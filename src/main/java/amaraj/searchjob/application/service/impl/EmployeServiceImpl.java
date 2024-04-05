package amaraj.searchjob.application.service.impl;

import amaraj.searchjob.application.dao.EmployeeRepository;
import amaraj.searchjob.application.dao.RoleRepository;
import amaraj.searchjob.application.dao.UserRepository;
import amaraj.searchjob.application.dto.employeDTO.CreateUpdateEmployeeDTO;
import amaraj.searchjob.application.dto.employeDTO.EmployeeDTO;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.entity.Application;
import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.entity.Employee;
//import amaraj.searchjob.application.entity.Role;
//import amaraj.searchjob.application.entity.User;
import amaraj.searchjob.application.entity.User;
import amaraj.searchjob.application.exception.EmployeeAlreadyExistsException;
import amaraj.searchjob.application.service.ApplicationService;
import amaraj.searchjob.application.service.EmployeService;
import amaraj.searchjob.application.service.TokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import static amaraj.searchjob.application.mapper.CompanyMapper.COMPANY_MAPPER;
import static amaraj.searchjob.application.mapper.EmployeeMapper.EMPLOYEE_MAPPER;
import static amaraj.searchjob.application.utils.PageUtils.toPageImpl;

@Service
@Validated
public class EmployeServiceImpl implements EmployeService {

    Logger LOGGER = LoggerFactory.getLogger(EmployeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TokenService tokenService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public PageDTO<EmployeeDTO> findAll(Pageable pageable) {
        return toPageImpl(employeeRepository.findAll(pageable),EMPLOYEE_MAPPER);    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }


    public String register(User user){
        if (userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Username already exists %s".formatted(user.getEmail()));
        }
        var userEmployee = User.builder()
                .userName(user.getUsername())
                .password(encoder.encode(user.getPassword()))
                .email(user.getEmail())
                .enabled(true)
                .roles(user.getRoles())
                .build();

        userRepository.save(userEmployee);
        Authentication authentication = authenticateUser(user.getEmail(), user.getPassword());
        String token = tokenService.generateToken(authentication);
        return token;
    }

    @Override
    public Optional<EmployeeDTO> findByNameAndEmail(String name, String email) {
        Optional<Employee> optionalEmployeeDto = employeeRepository.findByNameAndEmail(name, email);
        return optionalEmployeeDto.map(EMPLOYEE_MAPPER::toDTO);
    }

    private Authentication authenticateUser(String username, String password) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    @Transactional
    @Override
    public EmployeeDTO addEmployee(@Valid CreateUpdateEmployeeDTO employeeDTO) throws EmployeeAlreadyExistsException {

        if(employeeRepository.existsByEmail(employeeDTO.getEmail())){
            throw new EmployeeAlreadyExistsException("Another employee with the same email exists");
        }
        var userEntity = userRepository.findById(employeeDTO.getUser().getId()).get();
        var empEntity= EMPLOYEE_MAPPER.toEmployeeEntity(employeeDTO);
        empEntity.setUser(userEntity);
        return EMPLOYEE_MAPPER.toDTO(employeeRepository.save(empEntity));
    }

    @Transactional   //KA PROBLEM ME ADRESEN NULL   NK FUNKSIONON
    @Override
    public EmployeeDTO updateEmployee(Long empId, @Valid CreateUpdateEmployeeDTO req) {

        req.setJobSeekerId(empId);
        var emEntity = findById(empId).map(e ->{
            final var entity = e;
            e = EMPLOYEE_MAPPER.toEmployeeEntity(req);
            e.setName(req.getName());
            e.setSurname(req.getSurname());
            e.setAge(req.getAge());
            e.setEmail(req.getEmail());
         //   e.setFileOfCv(req.getFileOfCv());
         //   e.setAddress(req.getAdress());
            e.setYearsOfExp(req.getYearsOfExp());
            return e;
        }).map(employeeRepository::save)
                .orElseThrow(()->new RuntimeException(String.format("Cannot update employee with id %s", empId)));
        return EMPLOYEE_MAPPER.toDTO(emEntity);
    }

    @Override
    public void deleteEmployee(Long empId) {
        var employee = employeeRepository.findById(empId).orElseThrow(()-> new NoSuchElementException("Employee not found with ID: " + empId));
        employeeRepository.delete(employee);
    }

    @Override
    public List<Employee> listOfEmployees() {
        return employeeRepository.findAll();
    }



}
