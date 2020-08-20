package todo.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import todo.app.entity.Task;

import java.util.ArrayList;

public interface TaskService {
    ArrayList<Task> taskGetAll(Boolean flag);
    Page<Task> taskPagingAll(Boolean flag, Pageable pageable);
    void taskCRUD(Task task);
    Task taskGetById(int id);
}
