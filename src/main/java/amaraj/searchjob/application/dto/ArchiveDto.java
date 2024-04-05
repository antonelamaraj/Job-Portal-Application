package amaraj.searchjob.application.dto;

import amaraj.searchjob.application.entity.Employee;
import amaraj.searchjob.application.entity.Job;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArchiveDto {
    @NotNull
    private Long id;
    private Job job;
    private Employee employee;
    @NotNull
    @Column(name = "archived_date")
    private LocalDate archivedDate;


}
