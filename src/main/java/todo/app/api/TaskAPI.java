package todo.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import todo.app.constant.Constant;
import todo.app.entity.Task;
import todo.app.service.TaskServiceImp;
import todo.app.validation.Validation;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskAPI {
    @Autowired
    TaskServiceImp taskServiceImp;

    @GetMapping("/page")
    public ResponseEntity listPage(@RequestParam int pageInput) {
        Page<Task> taskPage = taskServiceImp.taskGetByPage(1);
        int totalPage = taskPage.getTotalPages();
        ArrayList<Integer> listPage = new ArrayList<>();
        if (pageInput == 1 && totalPage >= 3) {
            listPage.add(1);
            listPage.add(2);
            listPage.add(3);
        } else if (pageInput == totalPage && totalPage >= 3) {
            listPage.add(totalPage - 2);
            listPage.add(totalPage - 1);
            listPage.add(totalPage);
        } else if (pageInput > 1 && pageInput < totalPage && totalPage >= 3) {
            listPage.add(pageInput - 1);
            listPage.add(pageInput);
            listPage.add(pageInput + 1);
        } else if (totalPage < 3) {
            for (int i = 0; i < totalPage; i++) {
                listPage.add(i + 1);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(listPage);
    }

    @GetMapping("/tasks")
    public ResponseEntity tasksGetAll(Model model, @RequestParam(required = false) String page) {
        int indexPage;
        if (page != null) {
            indexPage = Integer.parseInt(page);
        } else {
            indexPage = 1;
        }
        Page<Task> taskPage = taskServiceImp.taskGetByPage(indexPage);
        List<Task> tasks = taskServiceImp.taskIndex(taskPage.getContent());
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/taskcrud")
    public ResponseEntity tasksCRUD(@RequestParam int id, @RequestParam String action, @RequestParam(required = false) String page, @RequestParam(required = false) String content) {
        Task task = new Task();
        if (id == 0) {
            Validation validation = new Validation();
            String subjectInput = validation.taskContentValidate(content);
            if (subjectInput.equals("EMPTY_ERROR")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constant.DATA_EMPTY);
            } else if (subjectInput.equals("LENGTH_ERROR")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constant.LENGTH_ERROR);
            } else {
                task.setContent(content.trim());
                task.setFlag(false);
                task.setStatus(false);
                taskServiceImp.taskCRUD(task);
                Page<Task> page1st = taskServiceImp.taskGetByPage(1);
                Page<Task> taskPage = taskServiceImp.taskGetByPage(page1st.getTotalPages());
                List<Task> tasks = taskServiceImp.taskIndex(taskPage.getContent());
                return ResponseEntity.status(HttpStatus.OK).body(tasks);
            }
        } else {
            task = taskServiceImp.taskGetById(id);
            if (action.equalsIgnoreCase("passed")) {
                task.setStatus(true);
            } else if (action.equalsIgnoreCase("failed")) {
                task.setStatus(false);
            } else if (action.equalsIgnoreCase("delete")) {
                task.setFlag(true);
            } else if (action.equalsIgnoreCase("create")) {
                Validation validation = new Validation();
                String subjectInput = validation.taskContentValidate(content);
                if (subjectInput.equals("EMPTY_ERROR")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constant.DATA_EMPTY);
                } else if (subjectInput.equals("LENGTH_ERROR")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constant.LENGTH_ERROR);
                } else {
                    task.setContent(content);
                }
            }
        }
        taskServiceImp.taskCRUD(task);
        Page<Task> taskPage = taskServiceImp.taskGetByPage(Integer.parseInt(page));
        if (taskPage.getContent().isEmpty()) {
            taskPage = taskServiceImp.taskGetByPage(Integer.parseInt(page) - 1);
        }
        List<Task> tasks = taskServiceImp.taskIndex(taskPage.getContent());
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

}
