package amaraj.searchjob.application.service.impl;

import amaraj.searchjob.application.dao.RoleRepository;
import amaraj.searchjob.application.dao.UserRepository;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.UserDto;
import amaraj.searchjob.application.entity.ChangePasswordRequest;
import amaraj.searchjob.application.entity.Role;
import amaraj.searchjob.application.entity.User;
import amaraj.searchjob.application.exception.UnauthorizedUserException;
import amaraj.searchjob.application.exception.UserAlreadyExistsException;
import amaraj.searchjob.application.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

import static amaraj.searchjob.application.mapper.JobMapper.JOB_MAPPER;
import static amaraj.searchjob.application.mapper.UserMapper.USER_MAPPER;
import static amaraj.searchjob.application.utils.PageUtils.toPageImpl;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).get();
    }


    @Override
    public void verifyUserCompany(Long companyId) throws UnauthorizedUserException {

        User currentUser = getCurrentUser();
        if (currentUser == null){
            throw new RuntimeException("User not found");
        }
        Long userCompanyId = currentUser.getCompany().getId();
        if (!userCompanyId.equals(companyId)){
            throw new UnauthorizedUserException("User is not authorized to perform operation on this company.");
        }
    }

    @Override
    public void verifyUserEmployee(Long employeeId) throws UnauthorizedUserException {
        User currentUser = getCurrentUser();
        if (currentUser == null){
            throw new RuntimeException("User not found");
        }
        Long userEmployeeId = currentUser.getEmployee().getJobSeekerId();
        if (!userEmployeeId.equals(employeeId)){
            throw new UnauthorizedUserException("User is not authorized to perform operation on this company.");
        }
    }

    //NUK FUKSIONON
    @Override
    public void verifyUserAdmin(Long userId) throws UnauthorizedUserException {
        User currentUser = getCurrentUser();
        if (currentUser == null){
            throw new RuntimeException("User not found");
        }
        Long userAdminId =currentUser.getId();
        if (!userAdminId.equals(userId)){
            throw new UnauthorizedUserException("User is not authorized to perform operation on this company.");
        }
    }

    @Override
    public long getTotalNumberOfUsers() {
        return userRepository.count();
    }

    @Transactional
    @Override
    public UserDto saveUser(User userDto, String role) throws UserAlreadyExistsException {

        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        Role userRole = roleRepository.findByName(role.toUpperCase()).orElse(null);

       if (userRole == null) {
            userRole = Role.builder()
                    .name("COMPANY")
                    .build();
            userRole = roleRepository.save(userRole);
        }

        // Create the user
        User createUser = User.builder()
                .userName(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .enabled(true)
                .build();

        createUser.setRoles(new HashSet<>(Arrays.asList(userRole)));
        createUser = userRepository.save(createUser);

        return USER_MAPPER.toDTO(createUser);
  }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public PageDTO<UserDto> findAll(Pageable pageable) {
        return toPageImpl(userRepository.findAll(pageable), USER_MAPPER);
    }

    @Override
    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(()-> new NoSuchElementException("User not found with ID: " + userId));
        userRepository.delete(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto req) {
        return findUserById(userId)
                .map(u -> {
                    u.setEmail(req.getEmail());
                    u.setUserName(req.getUserName());
                    u.setEnabled(true);
                    return userRepository.save(u);
                }).map(USER_MAPPER::toDTO).orElseThrow(() -> new RuntimeException(String.format("Cannot update user with id %s", userId)));

    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByUsername(name);
    }

}
