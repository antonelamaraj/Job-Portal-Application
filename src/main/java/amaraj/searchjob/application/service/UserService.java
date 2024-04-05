package amaraj.searchjob.application.service;

import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.UserDto;
import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.entity.ChangePasswordRequest;
import amaraj.searchjob.application.entity.Role;
import amaraj.searchjob.application.entity.User;
import amaraj.searchjob.application.exception.UnauthorizedUserException;
import amaraj.searchjob.application.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.Optional;

public interface UserService {

    UserDto saveUser(User userDto, String role) throws UserAlreadyExistsException;
    Optional<User> findUserById(Long id);
    PageDTO<UserDto> findAll(Pageable pageable);
    void deleteUser(Long userId);
    UserDto updateUser(Long userId, @Valid UserDto req);

    Optional<User> findUserByEmail(String email);
    Optional<User> findByName(String name);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);

    User getCurrentUser();
    void verifyUserCompany(Long companyId) throws UnauthorizedUserException;
    void verifyUserEmployee(Long employeeId) throws UnauthorizedUserException;
    void verifyUserAdmin(Long userId) throws UnauthorizedUserException;

    long getTotalNumberOfUsers();


}
