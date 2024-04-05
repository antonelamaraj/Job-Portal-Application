package amaraj.searchjob.application.dao;

import amaraj.searchjob.application.entity.Archive;
import org.apache.commons.lang3.arch.Processor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepository  extends JpaRepository<Archive, Long> {

    List<Archive> findByJobId(Long jobId);
}
