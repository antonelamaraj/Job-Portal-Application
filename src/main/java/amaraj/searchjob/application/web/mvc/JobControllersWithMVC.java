//package amaraj.searchjob.application.web.mvc;
//
//import amaraj.searchjob.application.dto.PageDTO;
//import amaraj.searchjob.application.dto.jobdto.JobDTO;
//import amaraj.searchjob.application.service.CompanyService;
//import amaraj.searchjob.application.service.JobService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import static amaraj.searchjob.application.mapper.JobMapper.JOB_MAPPER;
//
//@Controller
//@RequiredArgsConstructor
//public class JobControllersWithMVC {
//
//    private final JobService service;
//    private final CompanyService companyService;
//
////
////    //NUK FUNKSIONON
////    @GetMapping("/{jobID}")
////    public ResponseEntity<JobDTO> findById(@PathVariable Long jobID){
////        var emp = service.findById(jobID).map(JOB_MAPPER::toDTO).orElse(null);
////        return emp!=null?ResponseEntity.ok(emp):ResponseEntity.notFound().build();
////    }
//
//    @RequestMapping("/alljobs")
//    public ModelAndView getCompanies(
//            @RequestParam(required = false, defaultValue = "0") Integer page,
//            @RequestParam(required = false, defaultValue = "10") Integer size
//    ) {
//
//        var pageable = PageRequest.of(page, size);
//        PageDTO<JobDTO> pageDTO = service.findAll(pageable);
//        ModelAndView modelAndView = new ModelAndView("jobs");
//        modelAndView.addObject("pageDTO", pageDTO);
//        //System.out.println(pageDTO.getTotalElements());
//        return modelAndView;
//    }
//
//    @RequestMapping("job/{jobId}")
//    public ModelAndView findById(@PathVariable Long jobId) {
//        var job = service.findById(jobId).map(JOB_MAPPER::toDTO).orElse(null);
//        ModelAndView modelAndView = new ModelAndView();
//
//        if (job != null) {
//            modelAndView.addObject("job", job);
//            modelAndView.setViewName("jobHome");
//        } else {
//            modelAndView.setViewName("error");
//        }
//        return modelAndView;
//    }
//    @GetMapping("/job/form")
//    public ModelAndView showJobForm() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("job", new JobDTO());
//        modelAndView.setViewName("register_job_form");
//        return modelAndView;
//    }
//
//    //FUNKSIONON
//    @PostMapping("/saveJob")
//    public ModelAndView createJob(@Valid JobDTO req, BindingResult result) {
//        if (result.hasErrors()) {
//            ModelAndView errorModelAndView = new ModelAndView("register_job_form");
//            errorModelAndView.addObject("job", req);
//            return errorModelAndView;
//        } else {
//            JobDTO createdJob = service.addJob(req);
//            ModelAndView successModelAndView = new ModelAndView("redirect:/alljobs");
//            return successModelAndView;
//        }
//    }
//
//    @PostMapping("/createdJob/{companyId}")
//    public ModelAndView createJob(@PathVariable Long companyId, @ModelAttribute("jobDTO") JobDTO jobDTO) {
//        // Assuming you have a service method to fetch company information by companyId
//        var company = companyService.findCompanyByCompanyId(companyId).get();
//
//        // Set the company information into the job object
//        jobDTO.setCompany(company);
//
//        // Create the job using the service method
//        JobDTO createdJob = service.addJob(jobDTO);
//
//        // Redirect to a view to display the created job (you can customize this according to your needs)
//        ModelAndView modelAndView = new ModelAndView("register_job_form");
//        modelAndView.addObject("createdJob", createdJob);
//        return modelAndView;
//    }
//
//
////    @PutMapping("/{jobID}")
////    public ResponseEntity<JobDTO> updateJob(@PathVariable Long jobID, @RequestBody JobDTO req){
////        return ResponseEntity.ok(service.updateJob(jobID, req));
////    }
////
////    //NUK DEL REZULTAT
////
////    @DeleteMapping("/{jobId}")
////    public ResponseEntity<Void> deleteEmployee(@PathVariable Long jobId){
////        //  service.deleteJob(jobId);
////        return ResponseEntity.ok().build();
////    }
////
////    @GetMapping("/export/pdf")
////    public void exportToPDF(HttpServletResponse response) throws IOException {
////        PageDTO<JobDTO> jobList = service.findAll(PageRequest.of(0, 10));
////        JobPdfExporter exporter = new JobPdfExporter();
////        exporter.export(jobList, response);
////
////    }
//
//    /*
//     * Get all jobs
//     * Get a specific Job by id
//     * Create a new Job
//     * Delete a specific Jobby id
//     * Update a specific Job By id
//     * Get the company associated with a specificjob by id
//     *
//     * */
//}
