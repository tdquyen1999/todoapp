package todo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import todo.app.entity.Task;
import todo.app.repository.TaskRepository;

import java.util.ArrayList;
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


}
