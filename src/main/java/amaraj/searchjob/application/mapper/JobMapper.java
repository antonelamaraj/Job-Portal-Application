package amaraj.searchjob.application.mapper;

import amaraj.searchjob.application.dto.jobdto.CreateUpdateJobDTO;
import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface JobMapper extends BaseMapper<Job, JobDTO> {

    JobMapper JOB_MAPPER = Mappers.getMapper(JobMapper.class);

    @Override
    @Mapping(source = "entity.id", target = "id")
    JobDTO toDTO(Job entity);

    @Override
    @Mapping(source = "dto.id", target = "id")
    Job toEntity(JobDTO dto);


//    @Mapping(target = "id", source = "jobDTO.id")
//    Job toJobEntity(CreateUpdateJobDTO jobDTO);
////
//    @Mapping(target = "id", source = "job.id")
//    CreateUpdateJobDTO createUpdateJobDTOtoDto(Job job);

}
