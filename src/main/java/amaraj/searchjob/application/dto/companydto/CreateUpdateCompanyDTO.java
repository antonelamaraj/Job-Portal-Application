package amaraj.searchjob.application.dto.companydto;

//import amaraj.searchjob.application.entity.Address;
//import amaraj.searchjob.application.entity.Address;
import amaraj.searchjob.application.entity.User;
import amaraj.searchjob.application.entity.enumeration.Category;
import amaraj.searchjob.application.exception.DateNotValidException;
import amaraj.searchjob.application.exception.NiptNonValidException;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUpdateCompanyDTO {
    private Long id;
    @NotNull(message = "{company.validations.name}")
    @Size(min = 2, max = 30)
    private String name;
    @Lob
    @NotNull(message = "{company.validations.description}")
    private String description;
    @NotNull(message = "{company.validations.website}")
    @Pattern(regexp = "^[www\\.[a-zA-Z0-9]+\\.com$]")
    private String website;
    @NotNull(message = "{company.validations.contactInformation}")
    @Pattern(regexp = "^(068|069)\\d{7}$", message = "Invalid Albanian phone number")
    private String contactInformation;
    @NotNull(message = "{company.validations.category}")
    private Category category;
    @NotNull(message = "{company.validations.nipt}")
    @Column(unique = true)
    private String nipt;
    @NotNull
    private LocalDate openAt;
    @NotNull
    @Min(0)
   private Integer nrOfEmployees;
    @NotNull(message = "{company.validations.email}")
    @Email
    private String email;
    private User user;

    public CreateUpdateCompanyDTO(String name, String description, String website, String contactInformation, Category category,
                                  String nipt, LocalDate openAt, Integer nrOfEmployees, String email, User user
                                                                                                 ) throws NiptNonValidException {
        this.name = name;
        this.description = description;
        this.website = website;
        this.contactInformation = contactInformation;
        this.category = category;
        setNipt(nipt);
        setOpenAt(openAt);
            // Handle the exception, e.g., log it or throw another exception
         this.nrOfEmployees = nrOfEmployees;
        this.email = email;
        this.user = user;
    }


    @Override
    public String toString() {
        return "CreateUpdateCompanyDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", website='" + website + '\'' +
                ", contactInformation='" + contactInformation + '\'' +
                ", category=" + category +
                ", nipt='" + nipt + '\'' +
                ", openAt=" + openAt +
                ", nrOfEmployees=" + nrOfEmployees +
                ", email='" + email + '\'' +
                ", user=" + user +
                '}';
    }

    public void setNipt(String nipt) throws NiptNonValidException {
        if (isValidNiptFormat(nipt)) {
            this.nipt = nipt;
        } else {
            throw new NiptNonValidException("Nipt NUK eshte i sakte. \nDuhet te permbaje 10 karakteere");
        }
    }
//
//    public void setOpenAt(LocalDate openAt) throws DateNotValidException {
//        if (openAt == null ){
//            throw new DateNotValidException("Open At cannot be null");
//        }
//        try{
//            LocalDate currentDate = LocalDate.now();
//            if (openAt.isAfter(currentDate)){
//                throw new DateNotValidException("OpenAt date cannot ");
//        }
//            this.openAt = openAt;
//        }catch (DateTimeParseException e){
//            throw new DateNotValidException("openAt date is not in the correct format.");
//
//        }
//    }

    private static boolean isValidNiptFormat(String nipt) {
        if (nipt == null || nipt.length() != 10) {
            return false;
        }
        char firstChar = nipt.charAt(0);
        char lastChar = nipt.charAt(9);

        if (!Character.isLetter(firstChar) || !Character.isLetter(lastChar)) {
            return false;
        }

        String numbers = nipt.substring(1, 9);
        try {
            Integer.parseInt(numbers);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateUpdateCompanyDTO that)) return false;
        return name.equals(that.name) && nipt.equals(that.nipt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nipt);
    }

}
