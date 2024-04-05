package amaraj.searchjob.application;
//
//import amaraj.searchjob.application.dao.RoleRepository;
//import amaraj.searchjob.application.dao.UserRepository;
//import amaraj.searchjob.application.entity.Role;
//import amaraj.searchjob.application.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
public class SearchJobApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SearchJobApplication.class, args);
	}

//	@Autowired
//	PasswordEncoder encoder;
//
//	@Autowired
//	UserRepository userRepository;
//
//	@Autowired
//	RoleRepository roleRepository;

	@Transactional
	@Override
	public void run(String... args) throws Exception {

//		var roleEmployee = Role.builder()
//				.name("EMPLOYEE")
//				.build();
//		roleEmployee = roleRepository.save(roleEmployee);
//
//		var roleCompany = Role.builder()
//				.name("COMPANY")
//				.build();
//		roleCompany = roleRepository.save(roleCompany);
//
//		var roleAdmin = Role.builder()
//				.name("ADMIN")
//				.build();
//		roleAdmin = roleRepository.save(roleAdmin);
//
//		var employee = User.builder()
//				.email("employee@yahoo.com")
//				.userName("employee")
//				.enabled(true)
//				.password(encoder.encode("admin123"))
//				.build();
//		employee = userRepository.save(employee);
//        employee.setRoles(new HashSet<>(Arrays.asList(roleEmployee)));
//		userRepository.save(employee);
//		var company = User.builder()
//				.email("company@yahoo.com")
//				.userName("company")
//				.enabled(true)
//				.password(encoder.encode("admin123"))
//				.build();
//		company = userRepository.save(company);
//        company.setRoles(new HashSet<>(Arrays.asList(roleCompany)));
//		userRepository.save(company);
//		var admin = User.builder()
//				.email("admin@yahoo.com")
//				.userName("admin")
//				.enabled(true)
//				.password(encoder.encode("admin123"))
//				.build();
//		admin = userRepository.save(admin);
//		admin.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));
//		userRepository.save(admin);

	}
}
