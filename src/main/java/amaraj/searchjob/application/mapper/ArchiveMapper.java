package amaraj.searchjob.application.mapper;

import amaraj.searchjob.application.dto.ApplicationDTO;
import amaraj.searchjob.application.dto.ArchiveDto;
import amaraj.searchjob.application.entity.Application;
import amaraj.searchjob.application.entity.Archive;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArchiveMapper extends BaseMapper<Archive, ArchiveDto>{
    ArchiveMapper ARCHIVE_MAPPER = Mappers.getMapper(ArchiveMapper.class);

    @Override
    @Mapping(source = "entity.id", target = "id")
    ArchiveDto toDTO(Archive entity);

    @Override
    @Mapping(source = "dto.id", target = "id")
    Archive toEntity(ArchiveDto dto);
}
