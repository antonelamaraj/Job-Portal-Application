//package amaraj.searchjob.application.web.mvc;
//
//import amaraj.searchjob.application.dto.PageDTO;
//import amaraj.searchjob.application.dto.companydto.CompanyDto;
//import amaraj.searchjob.application.dto.companydto.CreateUpdateCompanyDTO;
//import amaraj.searchjob.application.dto.employeDTO.EmployeeDTO;
//import amaraj.searchjob.application.dto.jobdto.JobDTO;
//import amaraj.searchjob.application.entity.Company;
//import amaraj.searchjob.application.entity.Employee;
//import amaraj.searchjob.application.entity.Job;
//import amaraj.searchjob.application.entity.enumeration.Category;
//import amaraj.searchjob.application.entity.enumeration.ExperienceLevel;
//import amaraj.searchjob.application.entity.exporter.CompanyPdfExporter;
//import amaraj.searchjob.application.exception.DuplicateApplicationException;
//import amaraj.searchjob.application.mail.EmailSenderService;
//import amaraj.searchjob.application.service.ApplicationService;
//import amaraj.searchjob.application.service.CompanyService;
//import amaraj.searchjob.application.service.EmployeService;
//import amaraj.searchjob.application.service.JobService;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static amaraj.searchjob.application.mapper.CompanyMapper.COMPANY_MAPPER;
//
//@Controller
//@RequiredArgsConstructor
//public class CompanyControllerMVC {
//
//    private final CompanyService companyService;
//
//    private final JobService jobService;
//
//    private final EmployeService employeService;
//    private final ApplicationService applicationService;
//
//    private final EmailSenderService emailSenderService;
//
//
//
//    @RequestMapping("/allcompanies")
//    public ModelAndView getCompanies(
//            @RequestParam(required = false, defaultValue = "0") Integer page,
//            @RequestParam(required = false, defaultValue = "10") Integer size
//    ) {
//
//        var pageable = PageRequest.of(page, size);
//        PageDTO<CompanyDto> pageDTO = companyService.findAll(pageable);
//
//        ModelAndView modelAndView = new ModelAndView("companies");
//        modelAndView.addObject("pageDTO", pageDTO);
//        return modelAndView;
//    }
//
//        //OK
//    @RequestMapping("comp/{companyId}")//ose byName
//    public ModelAndView findById(@PathVariable Long companyId){
//        var comp = companyService.findCompanyByCompanyId(companyId).map(COMPANY_MAPPER::toDTO).orElse(null);
//        ModelAndView modelAndView = new ModelAndView();
//
//        if (comp!= null){
//            modelAndView.addObject("company", comp);
//            modelAndView.setViewName("companySettings");
//        }else {
//            modelAndView.setViewName("error");
//        }
//        return modelAndView;
//
//    }
//
//
//    //LISTO TE GJITHE JOBS E NJE KOMPANIE SPECIFIKE    FUNKSIONON
//    @GetMapping("comp/{companyId}/jobs")    //KLIKO MBI EMRIN
//    public ModelAndView getJobsByCompanyId(@PathVariable Long companyId) {
//        var comp = companyService.findCompanyByCompanyId(companyId).map(COMPANY_MAPPER::toDTO).orElse(null);
//        if (comp == null) {
//            // Company not found, handle accordingly (e.g., show an error page)
//            ModelAndView errorModelAndView = new ModelAndView("error");
//            return errorModelAndView;
//        }
//        ModelAndView modelAndView = new ModelAndView("companyHome");
//        List<JobDTO> jobs =  jobService.findJobsByCompanyId(comp.getId());
//        modelAndView.addObject("jobs", jobs);
//        modelAndView.addObject("company", comp); // Add the company ID to the model
//        return modelAndView;
//    }
//
//
//    //NUK FUNKSIONON SHTIMI I KOMPANISE
//    @GetMapping("/company/form")
//    public ModelAndView showCompanyForm() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("company", new CreateUpdateCompanyDTO());
//        modelAndView.setViewName("register_company_form");
//        return modelAndView;
//    }
//
//    @PostMapping("/saveCompany")
//        public ModelAndView createCompany(@Valid CreateUpdateCompanyDTO req, BindingResult result) {
//        //  ModelAndView errorModelAndView = new ModelAndView("register_company_form");
//        if (result.hasErrors()) {
//            ModelAndView errorModelAndView = new ModelAndView("register_company_form");
//            errorModelAndView.addObject("company", req);
//            return errorModelAndView;
//        } else {
//            CompanyDto createdCompany = companyService.addCompany(req);
//            ModelAndView successModelAndView = new ModelAndView("redirect:/allcompanies");
//            return successModelAndView;
//        }
//
//
//    }
//
//
////    @PutMapping("/{companyId}")
////    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long companyId, @RequestBody CreateUpdateCompanyDTO req){
////        return ResponseEntity.ok(companyService.updateCompany(companyId, req));
////    }
//
//
//    @PutMapping("/editComp/{id}")
//    public ModelAndView updateCompany(@PathVariable("id") Long id) {
//        ModelAndView modelAndView = new ModelAndView();
//
//        // Fetch the employee by ID from the service
//        Optional<Company> companyOptional = companyService.findCompanyByCompanyId(id);
//        if (companyOptional.isPresent()) {
//            Company company = companyOptional.get();
//            CreateUpdateCompanyDTO companyDTO = CreateUpdateCompanyDTO.builder()
//                    .name(company.getName())
//                    //  jobSeekerId(employee.getJobSeekerId())
//                    .nipt(company.getNipt())
//                            .category(company.getCategory())
//                                    .nrOfEmployees(company.getNrOfEmployees())
//                                            .description(company.getDescription())
//                                                    .website(company.getWebsite())
//                                                            .nrOfEmployees(company.getNrOfEmployees()).
//                            build();
//
//            modelAndView.addObject("companyDTO", companyDTO);
//            modelAndView.setViewName("register_company_form"); // Assuming "update-employee-form.html" is your form for updating employee details
//        } else {
//            // If employee is not found, set view to an error page or redirect to a different page
//            modelAndView.setViewName("company-not-found");
//        }
//
//        return modelAndView;
//    }
//
//    @PostMapping("/companies/{companyId}/update")
//    public ModelAndView updateCompany(@PathVariable Long companyId, @ModelAttribute CreateUpdateCompanyDTO companyDTO) {
//        // Update the company using the service method
//        CompanyDto updatedCompanyDto = companyService.updateCompany(companyId, companyDTO);
//
//        // Redirect to the company details page after updating
//        ModelAndView modelAndView = new ModelAndView("redirect:/allcompanies/{companyId}");
//        modelAndView.addObject("companyId", companyId);
//        return modelAndView;
//    }
//
//    /*********************Aplikimi per nje job posted **************************/
//    @PostMapping("comp/{companyId}/empl/{employeeId}/apply/{jobId}")
//    public String applyForJob(@PathVariable Long companyId, @PathVariable Long jobId, @PathVariable Long employeeId, Model model) throws DuplicateApplicationException {
//        Job job = jobService.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
//        Employee employee = employeService.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
//
//        applicationService.applyForJob(jobId, employeeId);
//        sendApplicationConfirmationEmail(job, employee);
//
//        model.addAttribute("message", "Success");
//        return "redirect:/companyHome"; // Replace with the name of your view
//    }
//
//    private void sendApplicationConfirmationEmail(Job job, Employee employee) {
//        String applicantEmail = employee.getEmail();
//        String companyName = job.getCompany().getName();
//        String companyEmail = job.getCompany().getEmail();
//        Long companyId = job.getCompany().getId();
//
//        String subject = "Application Confirmation";
//        String message = String.format("Thank you for applying for the " + job.getTitle() + " position. Your application has been received.\n" + companyName);
//        emailSenderService.sendEmail(companyId, companyEmail, applicantEmail, subject, message);
//    }
//
//
//    @GetMapping("/export/pdf")
//    public void exportToPDF(HttpServletResponse response) throws IOException {
//        PageDTO<CompanyDto> compList = companyService.findAll(PageRequest.of(0, 10));
//        CompanyPdfExporter exporter = new CompanyPdfExporter();
//        exporter.export(compList, response);
//
////        List<Employee>employeeList = employeService.listOfEmployees();
////        UserPdfExporter exporter = new UserPdfExporter();
////        exporter.export(employeeList, response);
//    }
//
////    @GetMapping("/export/excel")
////    public void exportToExcel(HttpServletResponse response) throws IOException {
////        PageDTO<CompanyDto> compList = companyService.findAll(PageRequest.of(0, 10));
////        CompanyExcelExporter exporter = new CompanyExcelExporter();
////        exporter.export(compList, response);
////
//////        List<Employee>employeeList = employeService.listOfEmployees();
//////        UserPdfExporter exporter = new UserPdfExporter();
//////        exporter.export(employeeList, response);
////    }
//
//
//
//    //    @RequestMapping("/allcompanies/chart")
////    public ModelAndView getCompaniesWithChart(
////            @RequestParam(required = false, defaultValue = "0") Integer page,
////            @RequestParam(required = false, defaultValue = "10") Integer size
////    ) {
////
////        var pageable = PageRequest.of(page, size);
////        List<String> companyName = companyService.findAll(pageable).getContent().stream().map(x->x.getCompany().getName()).collect(Collectors.toList());
////        List<Integer> jobPosted = companyService.findAll(pageable).getContent().stream().map(x->x.getCompany().getListOfJobs().size()).collect(Collectors.toList());
////
////        Map<List<String>, Integer> map = new HashMap<>();
////        map.put(companyName, jobPosted.indexOf());
////        ModelAndView modelAndView = new ModelAndView("barChart");
////        modelAndView.addObject("company",companyName );
////        modelAndView.addObject("jobs",jobPosted);
////        return modelAndView;
////    }
//}
