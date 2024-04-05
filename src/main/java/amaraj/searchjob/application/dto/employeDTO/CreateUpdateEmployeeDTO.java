package amaraj.searchjob.application.dto.employeDTO;

//import amaraj.searchjob.application.dto.AddressDTO;
//import amaraj.searchjob.application.entity.Address;

import amaraj.searchjob.application.dto.UserDto;
import amaraj.searchjob.application.entity.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUpdateEmployeeDTO {

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

  //  private String fileOfCv;
    private UserDto user;

    @Override
    public String toString() {
        return "CreateUpdateEmployeeDTO{" +
                "jobSeekerId=" + jobSeekerId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", yearsOfExp=" + yearsOfExp +
        //        ", fileOfCv='" + fileOfCv + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateUpdateEmployeeDTO that)) return false;
        return yearsOfExp == that.yearsOfExp && jobSeekerId.equals(that.jobSeekerId) && name.equals(that.name) && surname.equals(that.surname) && age.equals(that.age) && email.equals(that.email);  //&& fileOfCv.equals(that.fileOfCv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobSeekerId, name, surname, age, email, yearsOfExp);
    }
}
