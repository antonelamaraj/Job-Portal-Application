package amaraj.searchjob.application.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAreadyExistError {
    private String message;
    private Integer statusCode;
    private String path;
    private LocalDateTime timestamp;
}
