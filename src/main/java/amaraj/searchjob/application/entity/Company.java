package amaraj.searchjob.application.entity;

import amaraj.searchjob.application.dto.companydto.CreateUpdateCompanyDTO;
import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.entity.enumeration.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "companies")
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    @Size(min = 2, max = 30)
    private String name;
    @NotNull
    @Column(unique = true)
    private String nipt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Pattern(regexp = "[^www\\.[a-zA-Z0-9]+\\.com$]")
    private String website; //ky eshte nje link i klikueshem qe te con te faqja kryesore

    @Column(name = "open_at", nullable = false)
    private LocalDate openAt;
    @Email
    private String email;
    @Column(name = "nr_of_employees")
    private Integer nrOfEmployees;

    @Lob
    @Column(name = "description")
    @NotNull(message = "{company.validations.description}")
    private String description;
    @NotNull(message = "{company.validations.contactInformation}")
    @Pattern(regexp = "^(068|069)\\d{7}$", message = "Invalid Albanian phone number")
    private String contactInformation;
    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<Job> listOfJobs = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nipt='" + nipt + '\'' +
                ", category=" + category +
                ", website='" + website + '\'' +
                ", openAt=" + openAt +
                ", email='" + email + '\'' +
                ", nrOfEmployees=" + nrOfEmployees +
                ", description='" + description + '\'' +
                ", contactInformation='" + contactInformation + '\'' +
                ", user=" + user +
                '}';
    }

    public void addJobToCompany(Job job){
        if (listOfJobs == null){
            listOfJobs = new ArrayList<>();
        }
        listOfJobs.add(job);

        }


//Nje kompani mund te kete shume aplikante/ punekerkues
    //dhe nje punekerkues mund te aplikoje ne disa kompani

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "company_applicants",
//            joinColumns = @JoinColumn(name = "company_id"),
//            inverseJoinColumns = @JoinColumn(name = "job_seeker_id")
//    )
//    private Set<Employee> jobSeekerList;

//    @ManyToOne
//    @JoinColumn(name = "userId", referencedColumnName ="id")
//    private User user;



    //@Pattern()  me regex
    //12 karaktere 8 numra dhe 2 shkronja kapitale ne fillim dhe ne fund N12354625L

    //
//    public String getNipt() {
//        return nipt;
//    }
//
//    //Pattern per vendosjen e NIPT-it
//    public void setNipt(String nipt) throws NiptNonValidException {
//        if (isValidNiptFormat(nipt)) {
//            this.nipt = nipt;
//        } else {
//            throw new NiptNonValidException("Nipt NUK eshte i sakte. \nDuhet te permbaje 10 karakteere");
//        }
//    }
//
//    private static boolean isValidNiptFormat(String nipt) {
//        if (nipt == null || nipt.length() != 10) {
//            return false;
//        }
//        char firstChar = nipt.charAt(0);
//        char lastChar = nipt.charAt(9);
//
//        if (!Character.isLetter(firstChar) || !Character.isLetter(lastChar)) {
//            return false;
//        }
//
//        String numbers = nipt.substring(1, 9);
//        try {
//            Integer.parseInt(numbers);
//        } catch (NumberFormatException e) {
//            return false;
//        }
//        return true;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company company)) return false;
        return name.equals(company.name) && nipt.equals(company.nipt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nipt);
    }
}
