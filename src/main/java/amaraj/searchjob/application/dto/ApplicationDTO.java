package amaraj.searchjob.application.dto;

import amaraj.searchjob.application.entity.Employee;
import amaraj.searchjob.application.entity.Job;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ApplicationDTO {

    @NotNull
    private Long id;
    @NotNull
    private Job job;
    @NotNull
    private Employee employee;

    @Override
    public String toString() {
        return "ApplicationDTO{" +
                "id=" + id +
                ", job=" + job +
                ", employee=" + employee +
                '}';
    }
}
