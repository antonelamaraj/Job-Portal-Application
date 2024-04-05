package amaraj.searchjob.application.web;

import amaraj.searchjob.application.dao.UserRepository;
import amaraj.searchjob.application.dto.ArchiveDto;
import amaraj.searchjob.application.dto.companydto.CompanyDto;
import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.jobdto.CreateUpdateJobDTO;
import amaraj.searchjob.application.entity.Archive;
import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.entity.Employee;
import amaraj.searchjob.application.entity.Job;
import amaraj.searchjob.application.entity.enumeration.ExperienceLevel;
import amaraj.searchjob.application.entity.enumeration.JobType;
import amaraj.searchjob.application.entity.enumeration.Location;
import amaraj.searchjob.application.entity.exporter.JobPdfExporter;
import amaraj.searchjob.application.exception.DuplicateApplicationException;
import amaraj.searchjob.application.exception.UnauthorizedUserException;
import amaraj.searchjob.application.mail.EmailSenderService;
import amaraj.searchjob.application.mapper.JobMapper;
import amaraj.searchjob.application.service.*;
import amaraj.searchjob.application.utils.Tuple;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static amaraj.searchjob.application.mapper.JobMapper.JOB_MAPPER;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService service;
    private final CompanyService companyService;
    private final EmployeService employeService;

    private final ApplicationService applicationService;
    private final EmailSenderService emailSenderService;

    private final ArchiveService archiveService;

    private final UserService userService;

    @GetMapping("/{jobID}")
    public ResponseEntity<JobDTO> findById(@PathVariable Long jobID){
        var emp = service.findById(jobID).map(JOB_MAPPER::toDTO).orElse(null);
        return emp!=null?ResponseEntity.ok(emp):ResponseEntity.notFound().build();
    }

    //http://localhost:8080/api/jobs/job/title?title=Java
    @GetMapping("/job/title")
    public ResponseEntity<?>  findJobsByTitle(@RequestParam String title){
        List<JobDTO> jobs = service.findJobsByTitle(title);

        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There is not found any job with this title");
        } else {
            return ResponseEntity.ok(jobs);
        }
    }

    //http://localhost:8080/api/jobs/job/title/location?title=JAVA&location=ONSITE
    @GetMapping("/job/title/location")
    public ResponseEntity<?> findJobsByTitleAndLocation(
            @RequestParam String title,
            @RequestParam Location location) {

        List<JobDTO> jobs = service.findByTitleContainingIgnoreCaseAndLocation(title, location);

        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There are no jobs found with the specified title and location");
        } else {
            return ResponseEntity.ok(jobs);
        }
    }

    //http://localhost:8080/api/jobs/job/title/experienceLevel?title=JAVA&experienceLevel=NO_EXPERIENCE_REQUIRED
    @GetMapping("/job/title/experienceLevel")
    public ResponseEntity<?> findJobsByTitleAndExperienceLevel(@RequestParam String title, @RequestParam ExperienceLevel experienceLevel){
        List<JobDTO> jobs = service.findByTitleContainingIgnoreCaseAndExperienceLevel(title,experienceLevel);
        if (jobs.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There are no jobs found with the specified title and experience");
        }else{
            return ResponseEntity.ok(jobs);
        }
    }

    @GetMapping("/job/title/jobType")
    public ResponseEntity<?> findJobsByTitleAndJobType(@RequestParam String title, @RequestParam JobType jobType){
        List<JobDTO> jobs = service.findByTitleContainingIgnoreCaseAndJobType(title, jobType);
        if (jobs.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There are no jobs found with the specified title and Job type required");
        }else{
            return ResponseEntity.ok(jobs);
        }

    }

    //http://localhost:8080/api/jobs/job/title/jobType/location/experience?title=Spring&jobType=FULL_TIME&location=ONSITE&experience=SENIOR_LEVEL
    @GetMapping("/job/title/jobType/location/experience")
    public ResponseEntity<?> findByTitleContainingIgnoreCaseAndJobTypeAndLocationAndExperienceLevel(@RequestParam String title, @RequestParam JobType jobType, @RequestParam Location location, @RequestParam ExperienceLevel experience){
        List<JobDTO> jobs = service.findByTitleContainingIgnoreCaseAndJobTypeAndLocationAndExperienceLevel(title, jobType, location, experience);
        if (jobs.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There are no jobs found with the specified title and Job type required, location and experience level");
        }else{
            return ResponseEntity.ok(jobs);
        }
    }
    @GetMapping
    public ResponseEntity<PageDTO<JobDTO>> findAll(@RequestParam(required = false,defaultValue = "0")Integer page,
                                                        @RequestParam(required = false,defaultValue = "10")Integer size){
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.findAll(pageable));
    }

    //COMPANY
    @PostMapping("/createJob/{companyId}")
    public ResponseEntity<JobDTO> createJob(@PathVariable Long companyId, @RequestBody JobDTO job){
        var company = companyService.findCompanyByCompanyId(companyId).get();
        job.setCompany(company);
        return ResponseEntity.ok(service.addJob(job));
    }

    //COMPANY
    @PutMapping("/{jobID}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long jobID, @RequestBody JobDTO req){
        return ResponseEntity.ok(service.updateJob(jobID, req));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId){
        try {
            service.deleteJob(jobId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

   // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    //http://localhost:8080/api/jobs?page=0&size=30
    @GetMapping("/export/pdf")
    public void exportToPDF(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            HttpServletResponse response) throws IOException {
        PageDTO<JobDTO> jobList = service.findAll(PageRequest.of(page, size));
        JobPdfExporter exporter = new JobPdfExporter();
        exporter.export(jobList, response);
    }

    //Te gjitha jobs te postuara pas dates se dhene
    //http://localhost:8080/api/jobs/after-date/date?date=2024-03-15

    //DUEHT TE DAL DHE KOMPANIA QE E KA POSTUAR
    @GetMapping("/after-date/date")
    public ResponseEntity<?> getJobsAfterDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Job> jobs = service.getJobsPostedFromDate(date);

        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There are no jobs posted after the specified date");
        } else {
            return ResponseEntity.ok(jobs);
        }
    }

    //http://localhost:8080/api/jobs/title/date?title=java&date=2024-03-08
    @GetMapping("/title/date")
    public ResponseEntity<?> getJobsByTitleAndAfterDate(
            @RequestParam String title,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Job> jobs = service.getJobsPostedFromDate(title, date);

        if (jobs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There are no jobs found with the specified title and posted after the specified date");
        } else {
            return ResponseEntity.ok(jobs);
        }
    }

    /*********************Aplikimi per nje job posted ************* FUNKSIONOOOOOOOOON*************/
    @PostMapping("comp/{companyId}/empl/{employeeId}/apply/{jobId}")
    String applyForJob(@PathVariable Long companyId, @PathVariable Long jobId, @PathVariable Long employeeId) throws DuplicateApplicationException {
        //check nese exist job
        Optional<Job> jobOptional = service.findById(jobId);
        if (jobOptional.isEmpty()){
            return "Error: Job not found";
        }
            Job job = jobOptional.get();

        Optional<Employee> employee = employeService.findById(employeeId);

        if (employee.isEmpty()) {
            return "Error: Employee not found";
        }
        Employee empl = employee.get();

        Optional<Company> companyOptional = companyService.findCompanyByCompanyId(companyId);
        if (companyOptional.isEmpty()) {
            return "Error: Company not found";
        }
        Company company = companyOptional.get();

            if (job.getDateDeleted() != null && job.getDateDeleted().isBefore(LocalDate.now())){
                job.setStatus(false);
                JobDTO jobDTO = JOB_MAPPER.toDTO(job);
                service.updateJob(jobId,jobDTO);

                archiveService.archiveApplication(employeeId, jobId);
                sendApplicationReusedEmail(companyId, employeeId, jobId);

                return "The job is no longer active, you have been registered in the archive.";
            } else if (job.getApplications().stream().anyMatch(application -> application.getEmployee().getJobSeekerId().equals(employeeId))) {
                    return "Error: You have already applied for this job.";
                }else{

        applicationService.applyForJob(jobId, employeeId);
            sendApplicationConfirmationEmail(companyId, employeeId, jobId);
            return "Succes: Your application has been submitted.";
        }
    }

    private void sendApplicationConfirmationEmail(Long companyId, Long employeeId, Long jobId) {
        Employee employee = employeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        String aplicantEmail = employee.getEmail();

        Job job = service.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
        String companyName = job.getCompany().getName();
        String companyEmail = job.getCompany().getEmail();

        String subject = "Application Confirmation";
        String message = String.format("Thank you for applying for " + job.getTitle() + " position. Your application has been received.\n" + companyName);
        emailSenderService.sendEmail(companyId, companyEmail, aplicantEmail, subject, message);

    }

    private void sendApplicationReusedEmail(Long companyId, Long employeeId, Long jobId) {
        Employee employee = employeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        String aplicantEmail = employee.getEmail();

        Job job = service.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
        String companyName = job.getCompany().getName();
        String companyEmail = job.getCompany().getEmail();

        String subject = "Application Refused";
        String message = String.format("Thank you for applying for " + job.getTitle() + " position. Your application will be saved in our archives. \n" +
                "In a near future we will be in touch. Thanks in advance\n" + companyName);
        emailSenderService.sendEmail(companyId, companyEmail, aplicantEmail, subject, message);
    }

    /*********************Anullimi per nje job posted ************* FUNKSIONOOOOOOOOON*************/

 //   public ResponseEntity<String> deleteApplication(@PathVariable Long employeeId, @PathVariable Long applId) {
//        try {
//            // Check if the application exists and belongs to the specified employee
//            boolean isApplicationBelongsToEmployee = applicationService.isApplicationBelongsToEmployee(applId, employeeId);
//
//            if (!isApplicationBelongsToEmployee) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This isn't a job you have applied");
//            }
//
//            applicationService.deleteApplication(applId);
//            return ResponseEntity.ok("Your application is deleted");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete application");
//        }
//    }
    @DeleteMapping("/empl/{employeeId}/delete/{applId}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long employeeId, @PathVariable Long applId) {
        try {
            userService.verifyUserEmployee(employeeId);
            applicationService.deleteApplication(applId);
            return ResponseEntity.ok("Your application is deleted");
        } catch (UnauthorizedUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access to delete Employee.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting Employee.");
        }
    }

//     try {
//        userService.verifyUserEmployee(employeedId);
//        employeService.deleteEmployee(employeedId);
//        return ResponseEntity.ok("Employee deleted successfully.");
//    } catch (UnauthorizedUserException e) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access to delete Employee.");
//    }catch (Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting Employee.");
//    }

    //http://localhost:8080/api/jobs/archive/30   30 ->id e job
    @GetMapping("archive/{jobId}")  //permited for Companies that have posted that job
    public ResponseEntity<?> getArchivedApplications(@PathVariable Long jobId) throws UnauthorizedUserException {
        Optional<Job> jobOptional = service.findById(jobId);
        if (jobOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Job job = jobOptional.get();

        try {
            userService.verifyUserCompany(job.getCompany().getId());
        } catch (UnauthorizedUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to see archived Job Seekers for this job");
        }
        List<ArchiveDto> archived = archiveService.findArchivedApplicationsForJob(jobId);

        if (archived.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("There is no data archived for this job");
        }
        List<Tuple<Long, String, String, String, LocalDate>> tupleList = archived.stream()
                .map(dto -> new Tuple<>(dto.getId(), dto.getEmployee().getName(), dto.getEmployee().getEmail(), dto.getJob().getTitle(), dto.getArchivedDate()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(tupleList);
    }

}
