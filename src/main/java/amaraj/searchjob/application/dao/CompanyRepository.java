package amaraj.searchjob.application.dao;

import amaraj.searchjob.application.dto.companydto.CompanyDto;
import amaraj.searchjob.application.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findCompanyByNameAndNipt(String name, String nipt);

    boolean existsByNipt(String nipt);
}
