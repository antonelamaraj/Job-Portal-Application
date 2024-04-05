package amaraj.searchjob.application.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @Email
    @Column(nullable = false)
    private String email;
    //    @Column(nullable = false)
//    private String password;
    @NotNull(message = "{user.validations.userName}")
    private String userName;


    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
