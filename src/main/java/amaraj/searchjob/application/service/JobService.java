package amaraj.searchjob.application.service;

import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.jobdto.CreateUpdateJobDTO;
import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.entity.Job;
import amaraj.searchjob.application.entity.enumeration.ExperienceLevel;
import amaraj.searchjob.application.entity.enumeration.JobType;
import amaraj.searchjob.application.entity.enumeration.Location;
import amaraj.searchjob.application.exception.CompanyNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JobService {

    PageDTO<JobDTO> findAll(Pageable pageable);
    Optional<Job> findById(Long id);
    JobDTO addJob(@Valid JobDTO jobDTO);
    JobDTO updateJob(Long jobId, @Valid JobDTO req);
    void deleteJob(Long jobId);

    List<JobDTO> findJobsByCompanyId(Long companyId);

    JobDTO addJob(@Valid JobDTO jobDTO, Long companyId) throws CompanyNotFoundException;

    List<Job> getJobsPostedFromDate(LocalDate fromDate);

    List<Job> getJobsPostedFromDate(String title, LocalDate fromDate);

    List<JobDTO> findJobsByTitle(String title);
    List<JobDTO> findByTitleContainingIgnoreCaseAndLocation(String title, Location location);
    List<JobDTO> findByTitleContainingIgnoreCaseAndExperienceLevel(String title, ExperienceLevel experienceLevel);
    List<JobDTO> findByTitleContainingIgnoreCaseAndJobType(String title, JobType jobType);

    List<JobDTO>findByTitleContainingIgnoreCaseAndJobTypeAndLocationAndExperienceLevel(String title, JobType jobType, Location location, ExperienceLevel experienceLevel);

    Optional<JobDTO> findByTitleAndCompany(String title, Company company);

}
