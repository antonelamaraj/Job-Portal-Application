package amaraj.searchjob.application.service.impl;

import amaraj.searchjob.application.dao.CompanyRepository;
import amaraj.searchjob.application.dao.JobRepository;
import amaraj.searchjob.application.dao.RoleRepository;
import amaraj.searchjob.application.dao.UserRepository;
import amaraj.searchjob.application.dto.ApplicationDTO;
import amaraj.searchjob.application.dto.companydto.CompanyDto;
import amaraj.searchjob.application.dto.companydto.CompanyJobCountDTO;
import amaraj.searchjob.application.dto.companydto.CreateUpdateCompanyDTO;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.entity.*;
import amaraj.searchjob.application.exception.DateNotValidException;
import amaraj.searchjob.application.exception.DuplicateApplicationException;
import amaraj.searchjob.application.exception.NiptNonValidException;
import amaraj.searchjob.application.service.CompanyService;
import amaraj.searchjob.application.service.TokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static amaraj.searchjob.application.mapper.CompanyMapper.COMPANY_MAPPER;
import static amaraj.searchjob.application.mapper.EmployeeMapper.EMPLOYEE_MAPPER;
import static amaraj.searchjob.application.mapper.JobMapper.JOB_MAPPER;
import static amaraj.searchjob.application.mapper.UserMapper.USER_MAPPER;
import static amaraj.searchjob.application.utils.PageUtils.toPageImpl;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository repository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TokenService tokenService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Override
    public PageDTO<CompanyDto> findAll(Pageable pageable) {
        return toPageImpl(repository.findAll(pageable), COMPANY_MAPPER);
    }


    public String register(User user){
        if (userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Username already exists %s".formatted(user.getEmail()));
        }
        var userCompany = User.builder()
                .userName(user.getUsername())
                .password(encoder.encode(user.getPassword()))
                .email(user.getEmail())
                .enabled(true)
                .roles(user.getRoles())
                .build();

        userRepository.save(userCompany);
        Authentication authentication = authenticateUser(user.getEmail(), user.getPassword());
        String token = tokenService.generateToken(authentication);
        return token;
    }

    private Authentication authenticateUser(String username, String password) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    @Override
    public CompanyDto addCompany(@Valid CreateUpdateCompanyDTO req) throws DuplicateApplicationException, DateNotValidException, NiptNonValidException {
        
        if (repository.existsByNipt(req.getNipt())){
            throw new NiptNonValidException("Another company with the same nipt already exists.");
        }

        var userEntity = userRepository.findById(req.getUser().getId()).get();
        var compEntity = COMPANY_MAPPER.toCompanyEntity(req);
        compEntity.setUser(userEntity);

        return COMPANY_MAPPER.toDTO(repository.save(compEntity));
    }

    @Override
    public Optional<Company> findCompanyByCompanyId(Long id) {
        return repository.findById(id);
    }

    @Override
    public CompanyDto updateCompany(Long id, CreateUpdateCompanyDTO req) throws DateNotValidException {
        req.setId(id);
        var emEntity = findCompanyByCompanyId(id).map(e -> {
                    final var entity = e;
                    e = COMPANY_MAPPER.toCompanyEntity(req);
                    e.setName(req.getName());
                    e.setDescription(req.getDescription());
                    e.setNipt(req.getNipt());
                    e.setCategory(req.getCategory());
                    e.setWebsite(req.getWebsite());
                    e.setNrOfEmployees(req.getNrOfEmployees());
                    e.setOpenAt(req.getOpenAt());
                    return e;
                }).map(repository::save)
                .orElseThrow(() -> new RuntimeException(String.format("Cannot update company with id %s", id)));
        return COMPANY_MAPPER.toDTO(emEntity);
    }

    @Override
    public void deleteCompany(Long companyId) {
        //marrim kompanine me id
        var comp = repository.findById(companyId).orElseThrow(() -> new NoSuchElementException("Company not found with ID: " + companyId));
        repository.delete(comp);
    }

    @Override
    public List<ApplicationDTO> listofAllApplications(Long jobId) {
        return null;
    }

    @Override
    public Job postJob(Long companyId, Job job) {
        Company company = repository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not Found"));
        job.setCompany(company);
        //  jobRepository.saveAll(job);
        return null;
    }

    @Override
    public List<CompanyJobCountDTO> getCompaniesWithMostJobs(Long topN) {
        List<CompanyJobCountDTO> companyJobCounts = jobRepository.countJobsByCompany();
        companyJobCounts.sort(Comparator.comparingLong(CompanyJobCountDTO::getJobCount).reversed());
        return companyJobCounts.stream().limit(topN).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<String>> getJobsByCompany(Long companyId) {
        Company company = repository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));
        List<Job> jobs = jobRepository.findByCompanyId(companyId);
        Map<String, List<String>> companyJobsMap = new HashMap<>();

        List<String> jobTitles = jobs.stream().map(Job::getTitle).collect(Collectors.toList());
        companyJobsMap.put(company.getName(), jobTitles);
        return companyJobsMap;
    }

    @Override
    public Map<String, List<String>> getAllCompaniesWithJobs() {
        List<Company> companies = repository.findAll();
        Map<String, List<String>> companyJobsMap = new HashMap<>();
        for (Company company : companies) {
            List<JobProjection> jobs = jobRepository.findJobByCompanyId(company.getId());
            List<String> jobTitles = jobs.stream().map(JobProjection::getTitle).collect(Collectors.toList());
            companyJobsMap.put(company.getName(), jobTitles);
        }
        return companyJobsMap;
    }

    @Override
    public Optional<CompanyDto> findCompanyByNameAndNipt(String name, String nipt) {
        Optional<Company> optionalCompanyDto = repository.findCompanyByNameAndNipt(name, nipt);
        return optionalCompanyDto.map(COMPANY_MAPPER::toDTO);
    }
}
