package amaraj.searchjob.application.mapper;

import amaraj.searchjob.application.dto.BookmarkDTO;
import amaraj.searchjob.application.entity.Bookmark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookmarkMapper extends BaseMapper<Bookmark, BookmarkDTO> {

    BookmarkMapper BOOKMARK_MAPPER = Mappers.getMapper(BookmarkMapper.class);

//
    @Override
    @Mapping(source = "entity.id", target = "id")
    BookmarkDTO toDTO(Bookmark entity);

    @Override
    @Mapping(source = "dto.id", target = "id")
    Bookmark toEntity(BookmarkDTO dto);
}
