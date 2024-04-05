package amaraj.searchjob.application.web.advice;

import amaraj.searchjob.application.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ClassicModelsGlobalExceptionHanlder {

    @ExceptionHandler(NiptNonValidException.class)
    public ResponseEntity<NiptErrorMessage> handleNiptNonValidException(NiptNonValidException ex, HttpServletRequest request){
        var resp = NiptErrorMessage.builder()
                .message(ex.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI()).build();
        return ResponseEntity.badRequest().body(resp);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<NiptErrorMessage> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request){
        var resp = NiptErrorMessage.builder()
                .message(ex.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI()).build();
        return ResponseEntity.badRequest().body(resp);
    }


    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<CompanyErrorMessage> handleCompanyNonValidException(CompanyNotFoundException ex, HttpServletRequest request){
        var resp = CompanyErrorMessage.builder()
                .message(ex.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI()).build();
        return ResponseEntity.badRequest().body(resp);
    }


    @ExceptionHandler(DateNotValidException.class)
    public ResponseEntity<DateErrorMesssage> handleDateNotValidException(DateNotValidException ex, HttpServletRequest request){
        var resp = DateErrorMesssage.builder()
                .message(ex.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI()).build();
        return ResponseEntity.badRequest().body(resp);
    }

    @ExceptionHandler(EmployeeAlreadyExistsException.class)
    public ResponseEntity<EmployeeAreadyExistError> handleEmployeeAlreadyExistsException(EmployeeAlreadyExistsException ex, HttpServletRequest request){
        var resp = EmployeeAreadyExistError.builder()
                .message(ex.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI()).build();
        return ResponseEntity.badRequest().body(resp);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<UserErrorMesssage> handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest request){
        var resp = UserErrorMesssage.builder()
                .message(ex.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI()).build();
        return ResponseEntity.badRequest().body(resp);
    }


}
