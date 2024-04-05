package amaraj.searchjob.application.dto;

import amaraj.searchjob.application.entity.Employee;
import amaraj.searchjob.application.entity.Job;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class BookmarkDTO {

    private Long id;

    private Job jobListing;

    private Employee jobSeeker;
}
