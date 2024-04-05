package amaraj.searchjob.application.dao;

import amaraj.searchjob.application.entity.Application;
import amaraj.searchjob.application.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobId(Long jobId);
    Long countByJobId(Long jobId);
    List<Application> findApplicationsByEmployee_JobSeekerId(Long jobSeekerId);


}
