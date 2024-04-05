package amaraj.searchjob.application.mapper;

import amaraj.searchjob.application.dto.ApplicationDTO;
import amaraj.searchjob.application.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApplicationMapper extends BaseMapper<Application, ApplicationDTO> {
    ApplicationMapper APPLICATION_MAPPER = Mappers.getMapper(ApplicationMapper.class);

    @Override
    @Mapping(source = "entity.id", target = "id")
    ApplicationDTO toDTO(Application entity);

    @Override
    @Mapping(source = "dto.id", target = "id")
    Application toEntity(ApplicationDTO dto);
}
