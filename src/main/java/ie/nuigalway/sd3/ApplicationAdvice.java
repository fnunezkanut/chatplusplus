package ie.nuigalway.sd3;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ApplicationAdvice {


    //returns JSON error on a ApplicationException
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ApplicationException.class)
    public ApplicationResponse handleNotFoundException(ApplicationException e) {
        return new ApplicationResponse("error", e.getMessage()).noPayload();
    }
    //returns JSON error on a ApplicationException

}