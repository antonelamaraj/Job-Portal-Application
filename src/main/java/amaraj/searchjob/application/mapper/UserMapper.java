package amaraj.searchjob.application.mapper;

import amaraj.searchjob.application.dto.UserDto;
import amaraj.searchjob.application.dto.companydto.CompanyDto;
import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface UserMapper extends BaseMapper<User, UserDto> {
    UserMapper USER_MAPPER= Mappers.getMapper(UserMapper.class);


    @Override
    @Mapping(source = "dto.id", target = "id")
    User toEntity(UserDto dto);

    @Override
    @Mapping(source = "entity.id", target = "id")
    UserDto toDTO(User entity);
}
