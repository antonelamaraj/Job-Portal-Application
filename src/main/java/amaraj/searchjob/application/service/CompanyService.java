package amaraj.searchjob.application.service;

import amaraj.searchjob.application.dto.ApplicationDTO;
import amaraj.searchjob.application.dto.UserDto;
import amaraj.searchjob.application.dto.companydto.CompanyDto;
import amaraj.searchjob.application.dto.companydto.CompanyJobCountDTO;
import amaraj.searchjob.application.dto.companydto.CreateUpdateCompanyDTO;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.entity.Job;
import amaraj.searchjob.application.entity.User;
import amaraj.searchjob.application.exception.DateNotValidException;
import amaraj.searchjob.application.exception.DuplicateApplicationException;
import amaraj.searchjob.application.exception.NiptNonValidException;
import jakarta.validation.Valid;
import org.apache.catalina.UserDatabase;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CompanyService {

    PageDTO<CompanyDto> findAll(Pageable pageable);
    CompanyDto addCompany(@Valid CreateUpdateCompanyDTO req) throws DuplicateApplicationException, DateNotValidException, NiptNonValidException;
    Optional<Company> findCompanyByCompanyId(Long id);
    CompanyDto updateCompany(Long id, @Valid CreateUpdateCompanyDTO req) throws DateNotValidException;
    void deleteCompany(Long companyId);

    List<ApplicationDTO> listofAllApplications(Long jobId);

    Job postJob(Long companyId, Job job);
    List<CompanyJobCountDTO> getCompaniesWithMostJobs(Long topN);

    Map<String, List<String>> getJobsByCompany(Long companyId);

    Map<String, List<String>> getAllCompaniesWithJobs();

    Optional<CompanyDto> findCompanyByNameAndNipt(String name, String nipt);

    String register(User user);
}
