package amaraj.searchjob.application.web;

import amaraj.searchjob.application.dto.employeDTO.CreateUpdateEmployeeDTO;
import amaraj.searchjob.application.dto.employeDTO.EmployeeDTO;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.entity.Application;
import amaraj.searchjob.application.entity.Employee;
import amaraj.searchjob.application.entity.Job;
import amaraj.searchjob.application.entity.exporter.EmployeePdfExporter;
import amaraj.searchjob.application.entity.exporter.EmployeePdfExporterAsUser;
import amaraj.searchjob.application.exception.EmployeeAlreadyExistsException;
import amaraj.searchjob.application.exception.UnauthorizedUserException;
import amaraj.searchjob.application.service.ApplicationService;
import amaraj.searchjob.application.service.EmployeService;
import amaraj.searchjob.application.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static amaraj.searchjob.application.mapper.EmployeeMapper.EMPLOYEE_MAPPER;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    EmployeService employeService;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    UserService userService;

    @GetMapping("/{empId}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long empId){
        var emp = employeService.findById(empId).map(EMPLOYEE_MAPPER::toDTO).orElse(null);
        return emp!=null?ResponseEntity.ok(emp):ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<PageDTO<EmployeeDTO>> findAll(@RequestParam(required = false,defaultValue = "0")Integer page,
                                                        @RequestParam(required = false,defaultValue = "10")Integer size){
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(employeService.findAll(pageable));
    }

    @PostMapping   //OK
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody CreateUpdateEmployeeDTO req) throws EmployeeAlreadyExistsException {
        var empl = employeService.findByNameAndEmail(req.getName(), req.getEmail());
        if (empl.isPresent()) {
            throw new EmployeeAlreadyExistsException("This Employee already exists. Please check the data you have put?");
        }
        return ResponseEntity.ok(employeService.addEmployee(req));
    }

    //update nje empl
    @PutMapping("/{emplID}")   //OK
    public ResponseEntity<String> updateEmployee(@PathVariable Long emplID, @RequestBody CreateUpdateEmployeeDTO req){
        try {
            userService.verifyUserEmployee(emplID);
            employeService.updateEmployee(emplID, req);
            return ResponseEntity.ok("Employee updated successfully.");
        } catch (UnauthorizedUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access to update employee.");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating employee.");
        }
    }


    @DeleteMapping("/{employeedId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeedId){

        try {
            userService.verifyUserEmployee(employeedId);
            employeService.deleteEmployee(employeedId);
            return ResponseEntity.ok("Employee deleted successfully.");
        } catch (UnauthorizedUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access to delete Employee.");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting Employee.");
        }
    }


    //SHIKO TE GJITHA APLIKIMET E BERA NGA AI VETE
    //http://localhost:8080/api/employees/1/jobsApplied
    @GetMapping("/{employeeId}/jobsApplied")
    public ResponseEntity<?> allJobsApplied(@PathVariable Long employeeId){


        List<Application> applications = applicationService.findApplicationsByEmployee_JobSeekerId(employeeId);
        if (applications.isEmpty()){
            return ResponseEntity.ok("You haven't applied to any job yet");
        } else {
            List<Job> allJobs = applications.stream()
                    .map(Application::getJob)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(allJobs);
        }
    }

    @PostMapping("/uploadCV")
    public String handleFileUpload(@RequestParam("file")MultipartFile file){
        if (file.isEmpty()){
            return "Please select a file to upload";
        }
        try {
            String fileName = file.getOriginalFilename();
            byte[] fileContent = file.getBytes();
            return "File uploaded successfully: " + fileName;
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }

    //per ADMIN-in  shikoje dhe nje here se vetem 10 employee gjeneron
    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        PageDTO<EmployeeDTO> employeeList = employeService.findAll(PageRequest.of(0, 10));
        EmployeePdfExporterAsUser exporter = new EmployeePdfExporterAsUser();
        exporter.export( employeeList, response);
    }





}
