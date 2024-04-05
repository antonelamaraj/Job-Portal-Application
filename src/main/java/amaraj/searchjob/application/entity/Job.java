package amaraj.searchjob.application.entity;

import amaraj.searchjob.application.entity.enumeration.ExperienceLevel;
import amaraj.searchjob.application.entity.enumeration.JobType;
import amaraj.searchjob.application.entity.enumeration.Location;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "jobs_listing")
public class Job extends BaseEntity implements Comparable<Job>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;
    @NotBlank
    private String descritpion;
    private Double salary;
    @NotBlank
    private String skillsRequired;
    @NotNull
    @Enumerated(EnumType.STRING)
    private JobType jobType;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Location location;
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

    @NotNull
    private Integer vacance;
    private boolean status; //active / inactive
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;
    @JsonIgnore
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "job_applications",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    Set<Employee> jobSeekerList = new HashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy = "jobListing", cascade = CascadeType.ALL)
    private List<Bookmark>bookmarkList = new ArrayList<>();

    //A Job can have multiple applications
    @JsonIgnore
    @OneToMany(mappedBy = "job", fetch = FetchType.EAGER)
    private List<Application>applications = new ArrayList<>();


    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", descritpion='" + descritpion + '\'' +
                ", salary=" + salary +
                ", jobType=" + jobType +
                ", location=" + location +
                ", datePosted=" + datePosted +
                ", experienceLevel=" + experienceLevel +
                ", status=" + status +
                ", company=" + company +
                ", jobSeekerList=" + jobSeekerList +
                ", bookmarkList=" + bookmarkList +
                ", applications=" + applications +
                '}';
    }

    public void addApplicant(Employee employee){
        jobSeekerList.add(employee);
    }

    public void removeApplicant(Employee applicant) {
        jobSeekerList.remove(applicant);
    }

    @Override
    public int compareTo(Job otherJob) {
        return this.datePosted.compareTo(otherJob.datePosted);
    }
}
