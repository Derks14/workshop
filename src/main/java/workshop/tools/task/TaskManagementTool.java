package workshop.tools.task;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TaskManagementTool {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    private final AtomicLong taskIdGenerator = new AtomicLong(1);


    @Tool(description = "create a new task with title, description and assignee")
    public TaskResult createTask(String title, String description, String assignee){
        Long taskId = taskIdGenerator.getAndIncrement();

        Task task = new Task(taskId, title, description, assignee, TaskStatus.PENDING);
        tasks.put(taskId, task);

//        In real implementation, save to database and send notification email
        return new TaskResult(taskId, title, "PENDING", assignee,
                "Task created successfully and assigned to %s".formatted(assignee));
    }

    @Tool(description = "update task status by task id")
    public TaskResult updateStatus(Long taskId, TaskStatus status) {
        Task task = tasks.get(taskId);

        if (task == null) {
            return new TaskResult(taskId, "", "ERROR", "", "task not found");
        }

        Task updatedTask = Task.builder()
                .id(task.getId())
                .description(task.getDescription())
                .assignee(task.getAssignee())
                .status(task.getStatus())
                .build();

        tasks.put(taskId, updatedTask);

//        in real implementation: update database, trigger workflow and send notifications
        return new TaskResult(
                taskId,
                updatedTask.getTitle(),
                status.toString(),
                updatedTask.getAssignee(),
                "Task status updated to " + status);
    }


    @Tool(description = "assign or reassign a task to a different person")
    public TaskResult assignTask(Long taskId, String newAssignee) {
        Task task = tasks.get(taskId);

        if (task == null) {
            return new TaskResult(taskId, "", "ERROR", "", "task not found");
        }

        Task updatedTask = Task.builder()
                .id(task.getId())
                .title(task.getTitle())
                .assignee(task.getAssignee())
                .description(task.getDescription())
                .build();

        return new TaskResult(taskId,
                updatedTask.getTitle(),
                updatedTask.getStatus().toString(),
                newAssignee,
                "Task reassigned to %s".formatted(newAssignee));
    }

}
