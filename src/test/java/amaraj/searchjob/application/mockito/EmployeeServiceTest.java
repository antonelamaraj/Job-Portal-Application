package amaraj.searchjob.application.mockito;

import amaraj.searchjob.application.dao.EmployeeRepository;
import amaraj.searchjob.application.dto.employeDTO.CreateUpdateEmployeeDTO;
import amaraj.searchjob.application.dto.employeDTO.EmployeeDTO;
//import amaraj.searchjob.application.entity.Address;
import amaraj.searchjob.application.entity.Employee;
import amaraj.searchjob.application.exception.EmployeeAlreadyExistsException;
import amaraj.searchjob.application.service.EmployeService;
import amaraj.searchjob.application.service.impl.EmployeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Spy
    @InjectMocks
    EmployeService toTest = new EmployeServiceImpl();

    @Mock
    EmployeeRepository empRepo;


    @Test
    public void test_findAll_ok(){
        var emp = Arrays.asList(Employee.builder().jobSeekerId(1l).build());
        Page<Employee> pageEmployee = new PageImpl<>(emp);
        doReturn(pageEmployee).when(empRepo).findAll(any(Pageable.class));
        var output = toTest.findAll(PageRequest.of(0, 1));
        assertAll(
                ()->assertNotNull(output),
                ()->assertEquals(1, output.getContent().size()),
                ()->assertEquals(1, output.getContent().get(0).getJobSeekerId())
        );
    }

    //TESTI KALON POR NUK SHTOHET NE DB

    @Test
    public void test_Create_Employee() throws EmployeeAlreadyExistsException {
        var mockEntity = Employee.builder().jobSeekerId(1L).name("Era").yearsOfExp(2).build();
        doReturn(mockEntity).when(empRepo).save(any());
        var input = CreateUpdateEmployeeDTO.builder().name("Era").yearsOfExp(2).build();
                var output = toTest.addEmployee(input);

        assertAll(
                ()->assertNotNull(output),
                ()->assertEquals(1, output.getJobSeekerId()),
                ()->assertEquals("Era", output.getName()),
                ()->assertEquals(2, output.getYearsOfExp())
        );
    }

    //NUK Eshte i sakte

    @Test
    public void test_updateEmployee_ok(){
        var mockEntity = mock(Employee.class);
        doReturn(Optional.of(mockEntity)).when(toTest).findById(any());

        var savedEntity =Employee.builder().jobSeekerId(1L).surname("Lualashi").yearsOfExp(3).email("alulashi@gmail.com").build();
        doReturn(savedEntity).when(empRepo).save(any());
        var input = CreateUpdateEmployeeDTO.builder().jobSeekerId(1L).surname("Lualashi").yearsOfExp(3).email("alulashi@gmail.com").build();
        var output = toTest.updateEmployee(1L, input);

        assertAll(
                ()->assertNotNull(output.getJobSeekerId()),
                ()->assertEquals("Lualashi", output.getSurname()),
                ()->assertEquals(3, output.getYearsOfExp()),
                ()->assertEquals("alulashi@gmail.com", output.getEmail())
        );
    }




}
