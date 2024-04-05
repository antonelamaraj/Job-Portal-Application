package amaraj.searchjob.application.dto.jobdto;

import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.entity.Today;
import amaraj.searchjob.application.entity.enumeration.ExperienceLevel;
import amaraj.searchjob.application.entity.enumeration.JobType;
import amaraj.searchjob.application.entity.enumeration.Location;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter @Setter
@Builder
@AllArgsConstructor
public class JobDTO {
    
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String descritpion;
    @NotEmpty
    private Double salary;
    @Enumerated(EnumType.STRING)
    private JobType jobType;
    @NotNull
    @Column(name = "date_posted")
    @Today
    private LocalDate datePosted;  //last 24h, last 3 days, last 7 days
    @NotNull
    @Column(name = "date_deleted")
    @Future
    private LocalDate dateDeleted;
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;
    @NotBlank
    private String skillsRequired;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{job.validations.location}")
    private Location location;
    private boolean status; //active / inactive
    private Integer vacance;
    private Company company;


    public JobDTO() {
    }



}
