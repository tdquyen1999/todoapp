package todo.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.app.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task getById(int id);
    ArrayList<Task> findAllByFlag(Boolean flag);
    Page<Task> findAllByFlag(Boolean flag, Pageable pageable);
}
