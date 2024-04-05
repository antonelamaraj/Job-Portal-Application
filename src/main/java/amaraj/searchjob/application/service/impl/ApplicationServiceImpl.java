package amaraj.searchjob.application.service.impl;

import amaraj.searchjob.application.dao.ApplicationRepository;
import amaraj.searchjob.application.dao.JobRepository;
import amaraj.searchjob.application.dto.ApplicationDTO;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.entity.Application;
;
import amaraj.searchjob.application.entity.Job;
import amaraj.searchjob.application.exception.DuplicateApplicationException;
import amaraj.searchjob.application.service.ApplicationService;
import amaraj.searchjob.application.service.EmployeService;
import amaraj.searchjob.application.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static amaraj.searchjob.application.mapper.ApplicationMapper.APPLICATION_MAPPER;
import static amaraj.searchjob.application.mapper.JobMapper.JOB_MAPPER;
import static amaraj.searchjob.application.utils.PageUtils.toPageImpl;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final JobService jobService;
    private final EmployeService employeService;
    @Override
    public void applyForJob(Long jobId, Long employeeId) throws DuplicateApplicationException {
        Job job = jobService.findById(jobId).get();
        var empl = employeService.findById(employeeId);

        if (job.getJobSeekerList().contains(empl)){
            throw  new DuplicateApplicationException("Employee has already applied for this job.");
        }
        Application application = new Application();
        application.setJob(job);
        application.setEmployee(empl.get());
        application.setAppliedAt(LocalDate.now());
        applicationRepository.save(application);
        job.addApplicant(empl.get());
      //  empl.get().addJobs(job);
    }

    @Override
    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }

    @Override
    public void deleteApplication(Long applId) {
        var appl = applicationRepository.findById(applId).orElseThrow(() -> new NoSuchElementException("Application not found with ID: " + applId));
        applicationRepository.delete(appl);
    }

    @Override
    public PageDTO<ApplicationDTO> findAll(Pageable pageable) {
        return toPageImpl(applicationRepository.findAll(pageable), APPLICATION_MAPPER);
    }

    @Override
    public List<Application> findByJobId(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    @Override
    public List<Application> findApplicationsByEmployee_JobSeekerId(Long employeeId) {
        return  applicationRepository.findApplicationsByEmployee_JobSeekerId(employeeId);

    }


    private boolean employeeAlreadyApplied(Long jobId, Long employeeId){

        return false;
    }
    @Override
    public Long countByJobId(Long jobId) {
        return applicationRepository.countByJobId(jobId);
    }

    @Override
    public boolean isApplicationBelongsToEmployee(Long applicationId, Long employeeId) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);

        // If the application doesn't exist, return false
        if (applicationOptional.isEmpty()) {
            return false;
        }
        Application application = applicationOptional.get();

        // Check if the application's employee ID matches the specified employee ID
        return application.getEmployee().getJobSeekerId().equals(employeeId);
    }

//
//    @Override
//    public List<EmployeeDTO> findEmployeesByJobId(Long jobId) {
//        List<ApplicationDTO> jobApplications = applicationRepository.findEmployeesByJobId(jobId);
//        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
//
//        for (ApplicationDTO application: jobApplications){
//            EmployeeDTO employeeDTO = new EmployeeDTO();
//            employeeDTO.setJobSeekerId(application.getEmployee().getJobSeekerId());
//            employeeDTO.setName(application.getEmployee().getName());
//            employeeDTO.setSurname(application.getEmployee().getSurname());
//            employeeDTO.setEmail(application.getEmployee().getEmail());
//            employeeDTO.setYearsOfExp(application.getEmployee().getYearsOfExp());
//            employeeDTOs.add(employeeDTO);
//        }
//        return employeeDTOs;
//
//    }

}