package amaraj.searchjob.application.web;

import amaraj.searchjob.application.dao.RoleRepository;
import amaraj.searchjob.application.dao.UserRepository;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.UserDto;
import amaraj.searchjob.application.entity.Role;
import amaraj.searchjob.application.entity.User;
import amaraj.searchjob.application.service.TokenService;
import amaraj.searchjob.application.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static amaraj.searchjob.application.mapper.UserMapper.USER_MAPPER;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private  UserService userService;

    @PostMapping("/token")  //register
    public String generateToken(Authentication auth){
        var token = tokenService.generateToken(auth);
        return "Bearer ".concat(token);
    }

//    @PostMapping("/token")
//    public ResponseEntity<String> loginCompany(Authentication auth){
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        String token = tokenService.generateToken(auth);
//        return new ResponseEntity("Bearer ".concat(token), HttpStatus.OK);
//    }
    @GetMapping
    public ResponseEntity<PageDTO<UserDto>> getUsers(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.findAll(pageable));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findById(@PathVariable Long userId) {
        var user = userService.findUserById(userId).map(USER_MAPPER::toDTO).orElse(null);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserDto> saveUser(@RequestBody User userDto, @RequestParam String role) {
        try {
            UserDto savedUserDto = userService.saveUser(userDto, role);
            return ResponseEntity.ok(savedUserDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}