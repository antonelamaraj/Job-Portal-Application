package amaraj.searchjob.application.mapper;

import amaraj.searchjob.application.dto.employeDTO.CreateUpdateEmployeeDTO;
import amaraj.searchjob.application.dto.employeDTO.EmployeeDTO;
import amaraj.searchjob.application.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee, EmployeeDTO> {

    EmployeeMapper EMPLOYEE_MAPPER = Mappers.getMapper(EmployeeMapper.class);

    @Override
    @Mapping(source = "entity.jobSeekerId", target = "jobSeekerId")
    EmployeeDTO toDTO(Employee entity);

    @Override
    @Mapping(source = "dto.jobSeekerId", target = "jobSeekerId")
    Employee toEntity(EmployeeDTO dto);


    @Mapping(source = "dto.jobSeekerId", target = "jobSeekerId")
   // @Mapping(target = "address", ignore = true)
    Employee toEmployeeEntity(CreateUpdateEmployeeDTO dto);

    @Mapping(source = "entity.jobSeekerId", target = "jobSeekerId")
    CreateUpdateEmployeeDTO toCreateUpdateEmployeeDTO(Employee entity);


}

