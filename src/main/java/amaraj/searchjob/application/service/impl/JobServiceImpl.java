package amaraj.searchjob.application.service.impl;

import amaraj.searchjob.application.dao.JobRepository;
import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.entity.Job;
import amaraj.searchjob.application.entity.enumeration.ExperienceLevel;
import amaraj.searchjob.application.entity.enumeration.JobType;
import amaraj.searchjob.application.entity.enumeration.Location;
import amaraj.searchjob.application.exception.CompanyNotFoundException;
import amaraj.searchjob.application.service.CompanyService;
import amaraj.searchjob.application.service.JobService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static amaraj.searchjob.application.mapper.JobMapper.JOB_MAPPER;
import static amaraj.searchjob.application.utils.PageUtils.toPageImpl;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    CompanyService companyService;

    @Override
    public PageDTO<JobDTO> findAll(Pageable pageable) {
        return toPageImpl(jobRepository.findAll(pageable), JOB_MAPPER);
    }

    @Override
    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }


    //FIND JOB BY TITLE
    @Override
    public List<JobDTO> findJobsByTitle(String title) {
        List<Job> jobs = jobRepository.findByTitleContainingIgnoreCase(title);
        return jobs.stream().map(JOB_MAPPER::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<JobDTO> findByTitleContainingIgnoreCaseAndLocation(String title, Location location) {
        List<Job> jobs = jobRepository.findByTitleContainingIgnoreCaseAndLocation(title, location);
        return jobs.stream().map(JOB_MAPPER::toDTO).collect(Collectors.toList());

    }

    @Override
    public List<JobDTO> findByTitleContainingIgnoreCaseAndExperienceLevel(String title, ExperienceLevel experienceLevel) {
       List<Job> jobs = jobRepository.findByTitleContainingIgnoreCaseAndExperienceLevel(title, experienceLevel);
        return jobs.stream().map(JOB_MAPPER::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<JobDTO> findByTitleContainingIgnoreCaseAndJobType(String title, JobType jobType) {
        List<Job> jobs = jobRepository.findByTitleContainingIgnoreCaseAndJobType(title, jobType);
        return jobs.stream().map(JOB_MAPPER::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<JobDTO> findByTitleContainingIgnoreCaseAndJobTypeAndLocationAndExperienceLevel(String title, JobType jobType, Location location, ExperienceLevel experienceLevel) {
        List<Job> jobs = jobRepository.findByTitleContainingIgnoreCaseAndJobTypeAndLocationAndExperienceLevel(title, jobType, location, experienceLevel);
        return jobs.stream().map(JOB_MAPPER::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<JobDTO> findByTitleAndCompany(String title, Company company) {
        Optional<Job> jobOptional = jobRepository.findByTitleAndCompany(title, company);
        return jobOptional.map(JOB_MAPPER::toDTO);

    }


    @Transactional
    @Override
    public JobDTO addJob(JobDTO jobDTO) {
        var jobEntity = JOB_MAPPER.toEntity(jobDTO);
        var company = companyService.findCompanyByCompanyId(jobDTO.getCompany().getId())
                .orElseThrow(() -> new RuntimeException(String.format("Cannot add new Job, Company with id %s does not exist", jobDTO.getCompany())));
        jobEntity.setCompany(company);
        return JOB_MAPPER.toDTO(jobRepository.save(jobEntity));

        //DUHET te kontrolloje edhe kur ID e kompanise nuk ekziston
    }


    @Transactional
    @Override
    public JobDTO addJob(JobDTO jobDTO, Long companyId) throws CompanyNotFoundException {
        var jobEntity = JOB_MAPPER.toEntity(jobDTO);
        var company = companyService.findCompanyByCompanyId(companyId)
                .orElseThrow(() -> new RuntimeException(String.format("Cannot add new Job, Company with id %s does not exist", jobDTO.getCompany())));
        jobEntity.setCompany(company);
        return JOB_MAPPER.toDTO(jobRepository.save(jobEntity));
    }

    @Override
    public List<Job> getJobsPostedFromDate(LocalDate fromDate) {
       //marr te gjitha JObs
        List<Job> allJobs= jobRepository.findAll();
        List<Job> jobPostedFromDate = allJobs.stream().filter(job -> job.getDatePosted().isAfter(fromDate) || job.getDatePosted().isEqual(fromDate))
                .collect(Collectors.toList());
        return jobPostedFromDate;
    }

    @Override
    public List<Job> getJobsPostedFromDate(String title, LocalDate fromDate) {
        List<Job> allJobs = jobRepository.findByTitleContainingIgnoreCase(title);
        List<Job> jobPostedFromDate = allJobs.stream().filter(job -> job.getDatePosted().isAfter(fromDate) || job.getDatePosted().isEqual(fromDate))
                .collect(Collectors.toList());
        return jobPostedFromDate;
    }


    @Transactional
    @Override
    public JobDTO updateJob(Long jobId, JobDTO req) {
        return findById(jobId)
                .map(j -> {
                    j.setDescritpion(req.getDescritpion());
                    j.setJobType(req.getJobType());
                    j.setDatePosted(req.getDatePosted());
                    j.setDateDeleted(req.getDateDeleted());
                    j.setExperienceLevel(req.getExperienceLevel());
                    j.setTitle(req.getTitle());
                    j.setSalary(req.getSalary());
                    j.setLocation(req.getLocation());
                    j.setVacance(req.getVacance());
                    j.setSkillsRequired(req.getSkillsRequired());
                    return jobRepository.save(j);
                }).map(JOB_MAPPER::toDTO).orElseThrow(() -> new RuntimeException(String.format("Cannot update job with id %s", jobId)));
    }

    @Override
    public void deleteJob(Long jobId) {
        var job = jobRepository.findById(jobId).orElseThrow(()-> new NoSuchElementException("Job not found with ID: " + jobId));
        jobRepository.delete(job);
    }


    @Override
    public List<JobDTO> findJobsByCompanyId(Long companyId) {
        List<Job> jobs = jobRepository.findByCompanyId(companyId);
        return jobs.stream().map(JOB_MAPPER::toDTO).collect(Collectors.toList());
    }

}

