package amaraj.searchjob.application.mockito;

import amaraj.searchjob.application.dao.CompanyRepository;
import amaraj.searchjob.application.dto.companydto.CreateUpdateCompanyDTO;
import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.exception.DateNotValidException;
import amaraj.searchjob.application.exception.DuplicateApplicationException;
import amaraj.searchjob.application.exception.NiptNonValidException;
import amaraj.searchjob.application.service.CompanyService;
import amaraj.searchjob.application.service.impl.CompanyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Spy
    @InjectMocks
    CompanyService toTest = new CompanyServiceImpl();

    @Mock
    CompanyRepository compRepo;

    @Test
    public void test_findAll_ok(){
        var comp = Arrays.asList(Company.builder().id(1L).build());
        Page<Company> pageCompany = new PageImpl<>(comp);
        doReturn(pageCompany).when(compRepo).findAll(any(Pageable.class));
        var output = toTest.findAll(PageRequest.of(0, 1));
        assertAll(
                ()->assertNotNull(output),
                ()->assertEquals(1, output.getContent().size()),
                ()->assertEquals(1, output.getContent().get(0).getId())
        );
    }

    //TESTI KALON POR NUK SHTOHET NE DB

    @Test
    public void test_Create_Company() throws DuplicateApplicationException, NiptNonValidException, DateNotValidException {
        var mockEntity = Company.builder().id(1L).name("Ikub").nipt("L12345678l").build();
        doReturn(mockEntity).when(compRepo).save(any());
        var input = CreateUpdateCompanyDTO.builder().name("Ikub").nipt("L12345678l").build();
        var output = toTest.addCompany(input);

        assertAll(
                ()->assertNotNull(output),
                ()->assertEquals(1, output.getId()),
                ()->assertEquals("Ikub", output.getName()),
                ()->assertEquals("L12345678l", output.getNipt())
        );
    }
//


//    //NUK Eshte i sakte
    @Test
    public void test_updateCompany_ok() throws DateNotValidException {
        var mockEntity = mock(Company.class);
        doReturn(Optional.of(mockEntity)).when(toTest).findCompanyByCompanyId(any());

        var savedEntity =Company.builder().id(1L).name("Ikub").nipt("L12345678l").build();
        doReturn(savedEntity).when(compRepo).save(any());
        var input = CreateUpdateCompanyDTO.builder().id(1L).name("Ikub").nipt("L12345678l").build();
        var output = toTest.updateCompany(1L, input);

        assertAll(
                ()->assertNotNull(output.getId()),
                ()->assertEquals("Ikub", output.getName()),
                ()->assertEquals("L12345678l", output.getNipt())
        );
    }

}
