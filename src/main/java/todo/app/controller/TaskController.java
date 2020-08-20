package todo.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import todo.app.constant.Constant;
import todo.app.entity.Task;
import todo.app.exception.NotFoundException;
import todo.app.handler.ErrorResponse;
import todo.app.handler.TaskExceptionHandler;
import todo.app.service.TaskServiceImp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TaskController implements ErrorController {
    @Autowired
    TaskServiceImp taskServiceImp;
    @Autowired
    TaskExceptionHandler taskExceptionHandler;

    @RequestMapping("/test")
    public void test() throws NotFoundException {
        ArrayList<Task> tasks = taskServiceImp.taskGetAll(false);
        if(!tasks.isEmpty()) {
            throw new NotFoundException(Constant.NOT_FOUND_ANY_TASK);
        }
    }

    @RequestMapping("/home")
    public String home(Model model){
        Pageable pageable = PageRequest.of(0, Constant.PAGE_SIZE);
        Page<Task> taskPage = taskServiceImp.taskPagingAll(false, pageable);
        int totalPage = taskPage.getTotalPages();
        ArrayList<Integer> integers = new ArrayList<>();
        for(int i = 0; i < totalPage; i++) {
            integers.add(i + 1);
        }
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("lstPage", integers);
        return "index";
    }

    @RequestMapping("/error")
    public String error(){
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }

    public void tasksCheckEmpty(ArrayList<Task> tasks) throws NotFoundException {
        if(tasks.isEmpty()) {
            throw new NotFoundException(Constant.NOT_FOUND_ANY_TASK);
        }
    }


}
