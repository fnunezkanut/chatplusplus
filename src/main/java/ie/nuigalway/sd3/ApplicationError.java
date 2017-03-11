/**
 * @file ApplicationError.java
 *
 * shows custom error page instead of the default whitelabel error page
 */

package ie.nuigalway.sd3;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class ApplicationError implements ErrorController {


    private ErrorAttributes errorAttributes;
    private final static String ERROR_PATH = "/error";

    public ApplicationError(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }


    //html error view
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, ModelMap model) {

        HttpStatus status = getStatus(request);
        model.addAttribute("errorMsg", status.value() + " " + status.getReasonPhrase() );

        //return view name
        return new ModelAndView("_error");
    }

    //JSON, XML error view
    @RequestMapping(value = ERROR_PATH )
    @ResponseBody
    public ApplicationResponse error(HttpServletRequest request) {

        HttpStatus status = getStatus(request);
        return new ApplicationResponse( "error", status.value() + " " + status.getReasonPhrase() );
    }


    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


    private HttpStatus getStatus(HttpServletRequest request) {

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {

            try {

                return HttpStatus.valueOf(statusCode);
            } catch (Exception ex) {}
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}