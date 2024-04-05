package amaraj.searchjob.application.service;

import amaraj.searchjob.application.dto.ArchiveDto;
import amaraj.searchjob.application.entity.Archive;

import java.util.List;

public interface ArchiveService {

     void archiveApplication(Long employeeId, Long jobId);

     List<ArchiveDto> findArchivedApplicationsForJob(Long jobId);

}
