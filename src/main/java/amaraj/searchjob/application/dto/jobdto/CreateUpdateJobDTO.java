package amaraj.searchjob.application.dto.jobdto;

import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.entity.Today;
import amaraj.searchjob.application.entity.enumeration.ExperienceLevel;
import amaraj.searchjob.application.entity.enumeration.JobType;
import amaraj.searchjob.application.entity.enumeration.Location;
import amaraj.searchjob.application.exception.DateNotValidException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUpdateJobDTO {
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
    private Location location;
    private boolean status; //active / inactive
    private Integer vacance;
  //  public static Integer countApplicants = 0;
    private Company company;

    public CreateUpdateJobDTO(String title, String descritpion, Double salary, JobType jobType, LocalDate datePosted, LocalDate dateDeleted, ExperienceLevel experienceLevel, String skillsRequired, Location location, boolean status, Integer vacance, Company company) throws DateNotValidException {
        this.title = title;
        this.descritpion = descritpion;
        this.salary = salary;
        this.jobType = jobType;
        this.datePosted = datePosted;
        setDeadline(dateDeleted);
        this.experienceLevel = experienceLevel;
        this.skillsRequired = skillsRequired;
        this.location = location;
        this.status = status;
        this.vacance = vacance;
        this.company = company;
    }

    public void setDeadline(LocalDate dateDeleted) throws DateNotValidException {
        if(dateDeleted.compareTo(LocalDate.now()) < 0) {
            throw new DateNotValidException("Data nuk mund te jete me e vogel se data e sotme.");
        }
        this.dateDeleted = dateDeleted;	}




}
