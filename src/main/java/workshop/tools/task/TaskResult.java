package workshop.tools.task;

public record TaskResult(Long taskId, String title, String status, String assignee, String message) {
}
