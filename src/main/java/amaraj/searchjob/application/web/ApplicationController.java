package amaraj.searchjob.application.web;

import amaraj.searchjob.application.dto.ApplicationDTO;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.companydto.CompanyDto;
import amaraj.searchjob.application.dto.employeDTO.EmployeeDTO;
import amaraj.searchjob.application.entity.Application;
import amaraj.searchjob.application.entity.Employee;
import amaraj.searchjob.application.entity.Job;
import amaraj.searchjob.application.exception.UnauthorizedUserException;
import amaraj.searchjob.application.service.ApplicationService;
import amaraj.searchjob.application.service.JobService;
import amaraj.searchjob.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static amaraj.searchjob.application.mapper.ApplicationMapper.APPLICATION_MAPPER;
import static amaraj.searchjob.application.mapper.CompanyMapper.COMPANY_MAPPER;
import static amaraj.searchjob.application.mapper.EmployeeMapper.EMPLOYEE_MAPPER;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageDTO<ApplicationDTO>> getApplications(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(applicationService.findAll(pageable));
    }

    @DeleteMapping("/empl/{employeeId}/delete/{applId}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long employeeId, @PathVariable Long applId) {
        try {
            // Verify if the authenticated user is the owner of the application
            if (!applicationService.isApplicationBelongsToEmployee(applId, employeeId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This isn't a job you have applied");
            }
            applicationService.deleteApplication(applId);
            return ResponseEntity.ok("Your application is deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting Employee.");
        }
    }



    @GetMapping("/{applicationId}") //permit all
    public ResponseEntity<ApplicationDTO> findById(@PathVariable Long applicationId) {
        var application = applicationService.findById(applicationId).map(APPLICATION_MAPPER::toDTO).orElse(null);
        return application != null ? ResponseEntity.ok(application) : ResponseEntity.notFound().build();
    }



}
