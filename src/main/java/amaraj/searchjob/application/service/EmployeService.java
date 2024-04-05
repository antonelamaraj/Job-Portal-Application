package amaraj.searchjob.application.service;

import amaraj.searchjob.application.dto.companydto.CompanyDto;
import amaraj.searchjob.application.dto.employeDTO.CreateUpdateEmployeeDTO;
import amaraj.searchjob.application.dto.employeDTO.EmployeeDTO;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.entity.Application;
import amaraj.searchjob.application.entity.Employee;
import amaraj.searchjob.application.entity.User;
import amaraj.searchjob.application.exception.EmployeeAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;

public interface EmployeService {

    PageDTO<EmployeeDTO> findAll(Pageable pageable);
    Optional<Employee> findById(Long id);
    EmployeeDTO addEmployee(@Valid CreateUpdateEmployeeDTO employeeDTO) throws EmployeeAlreadyExistsException;
    EmployeeDTO updateEmployee(Long empId, @Valid CreateUpdateEmployeeDTO req);
    void deleteEmployee(Long empId);

    List<Employee> listOfEmployees();

    //List<Application> findByJobSeekerId(Long jobSeekerId);



    //METODA QE DUHEN KRIJUAR
 //   EmployeeDTO updateEmployeeWithBookmarkAndJobApplied(Long empId, @Valid CreateUpdateEmployeeDTO req);

    String register(User user);

    Optional<EmployeeDTO> findByNameAndEmail(String name, String email);
}
