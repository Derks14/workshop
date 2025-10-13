package workshop.tools.task;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Task {
    private Long id;
    private String title;
    private String description;
    private String assignee;
    private TaskStatus status;

}