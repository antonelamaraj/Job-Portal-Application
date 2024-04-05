package amaraj.searchjob.application.dto.employeDTO;

//import amaraj.searchjob.application.dto.AddressDTO;

//import amaraj.searchjob.application.entity.Address;

import amaraj.searchjob.application.dto.UserDto;
import amaraj.searchjob.application.entity.Employee;
import amaraj.searchjob.application.entity.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Builder
public class EmployeeDTO {
    // @NotNull(message = "{employee.validations.jobSeekerId}")
    private Long jobSeekerId;
    @NotNull(message = "{employee.validations.name}")
    @Size(min = 2, max = 30)
    private String name;
    @NotNull(message = "{employee.validations.surname}")
    @Size(min = 2, max = 30)
    private String surname;
    @NotNull
    @Min(18)
    private Integer age;

    @NotBlank(message = "{employee.validations.email}")
    @Email
    private String email;
    @NotNull(message = "{employee.validations.yearsOfExp}")
    @Min(0)
    private int yearsOfExp;
    private UserDto user;

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "jobSeekerId=" + jobSeekerId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", yearsOfExp=" + yearsOfExp +
                ", user=" + user +
                '}';
    }
}
