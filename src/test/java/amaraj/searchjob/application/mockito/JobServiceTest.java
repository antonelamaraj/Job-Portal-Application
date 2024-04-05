package amaraj.searchjob.application.mockito;

import amaraj.searchjob.application.dao.JobRepository;
import amaraj.searchjob.application.dto.jobdto.CreateUpdateJobDTO;
import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.entity.Job;
import amaraj.searchjob.application.entity.enumeration.JobType;
import amaraj.searchjob.application.service.JobService;
import amaraj.searchjob.application.service.impl.JobServiceImpl;
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
public class JobServiceTest {

    @Spy
    @InjectMocks
    JobService toTest = new JobServiceImpl();

    @Mock
    JobRepository jobRepo;


    @Test
    public void test_findAll_ok(){
        var job = Arrays.asList(Job.builder().id(1L).build());
        Page<Job> pageJob = new PageImpl<>(job);
        doReturn(pageJob).when(jobRepo).findAll(any(Pageable.class));
        var output = toTest.findAll(PageRequest.of(0, 1));
        assertAll(
                ()->assertNotNull(output),
                ()->assertEquals(1, output.getContent().size()),
                ()->assertEquals(1, output.getContent().get(0).getId())
        );
    }

    //TESTI KALON POR NUK SHTOHET NE DB

//    @Test
//    public void test_Create_Job(){
//        var mockEntity = Job.builder().id(1L).jobType(JobType.FULL_TIME).descritpion("Full time Job").salary(80.000).build();
//        doReturn(mockEntity).when(jobRepo).save(any());
//        var input = JobDTO.builder().jobType(JobType.FULL_TIME).descritpion("Full time Job").salary(80.000).build();
//        var output = toTest.addJob(input);
//
//        //DUHET TE SHTOJE DHE KOMPANINE
//        assertAll(
//                ()->assertNotNull(output),
//                ()->assertEquals(1, output.getId()),
//                ()->assertEquals(JobType.FULL_TIME, output.getJobType().name()),
//                ()->assertEquals("Full time Job", output.getDescritpion()),
//                ()->assertEquals(80.000, output.getSalary())
//        );
//    }



//    //    //NUK Eshte i sakte
//    @Test
//    public void test_updateJob_ok(){
//        var mockEntity = mock(Job.class);
//        doReturn(Optional.of(mockEntity)).when(toTest).findById(any());
//
//        var savedEntity =Job.builder().id(1L).jobType(JobType.FULL_TIME).descritpion("Full time Job").salary(80.000).build();
//        doReturn(savedEntity).when(jobRepo).save(any());
//        var input = CreateUpdateJobDTO.builder().id(1L).jobType(JobType.FULL_TIME).descritpion("Full time Job").salary(80.000).build();
//        var output = toTest.updateJob(1L, input);
//
//        assertAll(
//                ()->assertNotNull(output),
//                ()->assertEquals(1, output.getId()),
//                ()->assertEquals(JobType.FULL_TIME, output.getJobType().name()),
//                ()->assertEquals("Full time Job", output.getDescritpion()),
//                ()->assertEquals(80.000, output.getSalary())
//        );
//    }
}
