package todo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import todo.app.constant.Constant;
import todo.app.entity.Task;
import todo.app.repository.TaskRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImp implements TaskService{
    @Autowired
    TaskRepository taskRepository;

    @Override
    public ArrayList<Task> taskGetAll(Boolean flag) {
        return taskRepository.findAllByFlag(flag);
    }

    @Override
    public Page<Task> taskPagingAll(Boolean flag, Pageable pageable) {
        return taskRepository.findAllByFlag(flag, pageable);
    }

    @Override
    public void taskCRUD(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task taskGetById(int id) {
        return taskRepository.getById(id);
    }

    public Page taskGetByPage(int indexPage) {
        Pageable pageable = PageRequest.of(indexPage - 1, Constant.PAGE_SIZE);
        Page<Task> taskPage = taskPagingAll(false, pageable);
        return taskPage;
    }

    public List<Task> taskIndex(List<Task> tasks) {
        Map<Integer, Task> integerTaskMap = new HashMap<>();
        ArrayList<Task> tasks1 = taskGetAll(false);
        for(int i = 0; i < tasks1.size(); i++) {
            integerTaskMap.put((i + 1), tasks1.get(i));
        }
        for(Map.Entry<Integer, Task> entry : integerTaskMap.entrySet()) {
            for(Task task : tasks) {
                if(entry.getValue().getId() == task.getId()) {
                    task.setIndex(entry.getKey());
                }
            }
        }
        return tasks;
    }
}
