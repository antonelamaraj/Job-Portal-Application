package amaraj.searchjob.application.entity;

import amaraj.searchjob.application.exception.DuplicateApplicationException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobSeekerId;
    @Size(min = 2, max = 30)
    @NotNull(message = "{employee.validations.name}")
    private String name;

    @NotNull(message = "{employee.validations.surname}")
    @Size(min = 2, max = 30)
    private String surname;
    @Min(18)
    @NotNull(message = "{employee.validations.age}")
    private Integer age;
    @NotBlank
    @Email(message = "{employee.validations.email}")
    private String email;
    @Min(0)
    private int yearsOfExp;
    private String fileOfCv;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "job_applications",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id"))
    private List<Job> jobApplied = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Application> applications = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    // A job seeker can have multiple bookmarks.
    @JsonIgnore
    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @Override
    public String toString() {
        return "Employee{" +
                "jobSeekerId=" + jobSeekerId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", yearsOfExp=" + yearsOfExp +
                ", fileOfCv='" + fileOfCv + '\'' +
                ", jobApplied=" + jobApplied +
                ", user=" + user +
                ", bookmarkList=" + bookmarkList +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobSeekerId, name, surname, age, email, yearsOfExp, fileOfCv, jobApplied, bookmarkList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return yearsOfExp == employee.yearsOfExp && jobSeekerId.equals(employee.jobSeekerId) &&
                name.equals(employee.name) && surname.equals(employee.surname) && age.equals(employee.age) &&
                email.equals(employee.email) && fileOfCv.equals(employee.fileOfCv)&&
                jobApplied.equals(employee.jobApplied) && bookmarkList.equals(employee.bookmarkList);
    }

}
