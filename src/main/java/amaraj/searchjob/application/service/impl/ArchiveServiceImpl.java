package amaraj.searchjob.application.service.impl;

import amaraj.searchjob.application.dao.ArchiveRepository;
import amaraj.searchjob.application.dao.EmployeeRepository;
import amaraj.searchjob.application.dao.JobRepository;
import amaraj.searchjob.application.dto.ArchiveDto;
import amaraj.searchjob.application.entity.Application;
import amaraj.searchjob.application.entity.Archive;
import amaraj.searchjob.application.entity.Employee;
import amaraj.searchjob.application.service.ArchiveService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static amaraj.searchjob.application.mapper.ArchiveMapper.ARCHIVE_MAPPER;
import static amaraj.searchjob.application.mapper.EmployeeMapper.EMPLOYEE_MAPPER;
import static amaraj.searchjob.application.mapper.JobMapper.JOB_MAPPER;

@Service
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    ArchiveRepository archiveRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    JobRepository jobRepository;

    @Override
    public void archiveApplication(Long employeeId, Long jobId) {
        var empId = employeeRepository.findById(employeeId);
        var joId = jobRepository.findById(jobId);
        Archive archivedApplication = new Archive();
        archivedApplication.setEmployee(empId.get());
        archivedApplication.setJob(joId.get());
        archivedApplication.setArchivedDate(LocalDate.now());

        archiveRepository.save(archivedApplication);
    }

    @Override
    public List<ArchiveDto> findArchivedApplicationsForJob(Long jobId) {
        List<Archive> archiveList = archiveRepository.findByJobId(jobId);
         return archiveList.stream().map(ARCHIVE_MAPPER::toDTO).collect(Collectors.toList());
    }



}
