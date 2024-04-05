package amaraj.searchjob.application.dto.companydto;

import amaraj.searchjob.application.entity.Company;
import lombok.*;

@AllArgsConstructor
@Getter @Setter
@Data
public class CompanyJobCountDTO {

    private final Company company;
    private final Long jobCount;

}
