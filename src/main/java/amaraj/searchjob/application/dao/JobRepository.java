package amaraj.searchjob.application.dao;

import amaraj.searchjob.application.dto.companydto.CompanyJobCountDTO;
import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.entity.Job;
import amaraj.searchjob.application.entity.JobProjection;
import amaraj.searchjob.application.entity.enumeration.ExperienceLevel;
import amaraj.searchjob.application.entity.enumeration.JobType;
import amaraj.searchjob.application.entity.enumeration.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCompanyId(Long companyId);
    List<JobDTO> findJobsByCompanyId(Long companyId);
    List<JobProjection> findJobByCompanyId(Long companyId);

    @Query("SELECT new amaraj.searchjob.application.dto.companydto.CompanyJobCountDTO(j.company, COUNT(j)) " +
            "FROM Job j GROUP BY j.company")
    List<CompanyJobCountDTO> countJobsByCompany();

    List<Job> findByTitleContainingIgnoreCase(String title);
    List<Job> findByTitleContainingIgnoreCaseAndLocation(String title, Location location);
    List<Job> findByTitleContainingIgnoreCaseAndExperienceLevel(String title, ExperienceLevel experienceLevel);
    List<Job> findByTitleContainingIgnoreCaseAndJobType(String title, JobType jobType);
    List<Job>findByTitleContainingIgnoreCaseAndJobTypeAndLocationAndExperienceLevel(String title, JobType jobType, Location location, ExperienceLevel experienceLevel);
    Optional<Job> findByTitleAndCompany(String title, Company company);


}
