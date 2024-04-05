package amaraj.searchjob.application.web;

import amaraj.searchjob.application.dao.UserRepository;
import amaraj.searchjob.application.dto.PageDTO;
import amaraj.searchjob.application.dto.UserDto;
import amaraj.searchjob.application.dto.companydto.CompanyDto;
import amaraj.searchjob.application.dto.employeDTO.CreateUpdateEmployeeDTO;
import amaraj.searchjob.application.dto.employeDTO.EmployeeDTO;
import amaraj.searchjob.application.exception.UnauthorizedUserException;
import amaraj.searchjob.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static amaraj.searchjob.application.mapper.CompanyMapper.COMPANY_MAPPER;
import static amaraj.searchjob.application.mapper.UserMapper.USER_MAPPER;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
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



    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully.");
    }

    @GetMapping("/count")
    public long getTotalNumberOfUsers() {
        return userService.getTotalNumberOfUsers();
    }


}
