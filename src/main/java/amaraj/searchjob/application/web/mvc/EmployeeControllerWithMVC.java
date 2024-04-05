//package amaraj.searchjob.application.web.mvc;
//
//import amaraj.searchjob.application.dto.PageDTO;
//import amaraj.searchjob.application.dto.employeDTO.CreateUpdateEmployeeDTO;
//import amaraj.searchjob.application.dto.employeDTO.EmployeeDTO;
//import amaraj.searchjob.application.service.EmployeService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import static amaraj.searchjob.application.mapper.EmployeeMapper.EMPLOYEE_MAPPER;
//@Controller
//public class EmployeeControllerWithMVC {
//    @Autowired
//    EmployeService employeService;
//
////
////    @GetMapping("/home")
////    public String home(Model model) {
////        model.addAttribute("message", "Hello, Thymeleaf!");
////        return "home"; // This will render the "home.html" template in the "templates" directory
////    }
//
//    @RequestMapping("/allemployees")
//    public ModelAndView findAllEmployees(
//            @RequestParam(required = false, defaultValue = "0") Integer page,
//            @RequestParam(required = false, defaultValue = "10") Integer size
//          ) {
//
//        var pageable = PageRequest.of(page, size);
//        PageDTO<EmployeeDTO> pageDTO = employeService.findAll(pageable);
//        ModelAndView modelAndView = new ModelAndView("employees");
//        modelAndView.addObject("pageDTO", pageDTO);
//        //System.out.println(pageDTO.getTotalElements());
//        return modelAndView;
//    }
//
//
//    @RequestMapping("emp/{empId}")
//    public ModelAndView findById(@PathVariable Long empId){
//        var emp = employeService.findById(empId).map(EMPLOYEE_MAPPER::toDTO).orElse(null);
//        ModelAndView modelAndView = new ModelAndView();
//
//        if (emp!= null){
//            modelAndView.addObject("employee", emp);
//            modelAndView.setViewName("employeeHome");
//        }else {
//            modelAndView.setViewName("error");
//        }
//        return modelAndView;
//    }
//
//
//    @GetMapping("/employees/form")
//    public ModelAndView showEmployeeForm() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("employee", new CreateUpdateEmployeeDTO());
//        modelAndView.setViewName("registration_empl_form");
//        return modelAndView;
//    }
//
//    @PostMapping("/save")
//    public ModelAndView createEmployee(@Valid CreateUpdateEmployeeDTO req, BindingResult result) {
//        if (result.hasErrors()) {
//            ModelAndView errorModelAndView = new ModelAndView("registration_empl_form");
//            errorModelAndView.addObject("employee", req);
//            return errorModelAndView;
//        } if(req.getJobSeekerId()==null) {
//            EmployeeDTO createdEmployee = employeService.addEmployee(req);
//        }
//        else{
//            employeService.updateEmployee(req.getJobSeekerId(), req);
//
//        }
//        ModelAndView successModelAndView = new ModelAndView("redirect:/allemployees");
//        return successModelAndView;
//    }
//
//
////    //NUK DEL REZULTAT
////
////    @DeleteMapping("/{employeedId}")
////    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer employeedId){
////        return ResponseEntity.ok().build();
////    }
////
////
////    //upload a CV
////
////    @PostMapping("/uploadCV")
////    public String handleFileUpload(@RequestParam("file") MultipartFile file){
////        if (file.isEmpty()){
////            return "Please select a file to upload";
////        }
////        try {
////            // You can access file information like filename, content type, and bytes
////            String fileName = file.getOriginalFilename();
////            byte[] fileContent = file.getBytes();
////            // Process the file content as needed
////
////            // Example: Saving the file to a directory
////            // Files.write(Paths.get("/path/to/save/" + fileName), fileContent);
////
////            return "File uploaded successfully: " + fileName;
////        } catch (IOException e) {
////            return "Failed to upload file: " + e.getMessage();
////        }
////    }
////
////    @GetMapping("/export/pdf")
////    public void exportToPDF(HttpServletResponse response) throws IOException {
////        PageDTO<EmployeeDTO> employeeList = employeService.findAll(PageRequest.of(0, 10));
////        EmployeePdfExporter exporter = new EmployeePdfExporter();
////        exporter.export(employeeList, response);
////
//////        List<Employee>employeeList = employeService.listOfEmployees();
//////        UserPdfExporter exporter = new UserPdfExporter();
//////        exporter.export(employeeList, response);
////    }
//}
