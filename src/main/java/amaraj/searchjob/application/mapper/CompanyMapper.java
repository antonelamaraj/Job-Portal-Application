package amaraj.searchjob.application.mapper;

import amaraj.searchjob.application.dto.companydto.CompanyDto;
import amaraj.searchjob.application.dto.companydto.CreateUpdateCompanyDTO;
import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.exception.DateNotValidException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompanyMapper extends  BaseMapper<Company, CompanyDto> {
    CompanyMapper COMPANY_MAPPER= Mappers.getMapper(CompanyMapper.class);


    @Override
    @Mapping(source = "entity.id", target = "id")
    CompanyDto toDTO(Company entity);

    @Override
    @Mapping(source = "dto.id", target = "id")
    Company toEntity(CompanyDto dto);

    @Mapping(source = "dto.id", target = "id")
    Company toCompanyEntity(CreateUpdateCompanyDTO dto);

    @Mapping(source = "entity.id", target = "id")
    CreateUpdateCompanyDTO toCreateUpdateCompanyDTO(Company entity);

}
