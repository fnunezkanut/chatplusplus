package ie.nuigalway.sd3.controllers;

import ie.nuigalway.sd3.ApplicationException;
import ie.nuigalway.sd3.ApplicationResponse;
import ie.nuigalway.sd3.entities.User;
import ie.nuigalway.sd3.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;

@RestController
public class LoginSubmitController {

    @Autowired
    private UserService userService;

    //receives email+pass via ajax POST
    @RequestMapping(method = RequestMethod.POST, value = "/login/submit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApplicationResponse action(@RequestParam(value = "email") String email, @RequestParam(value = "pass") String pass, HttpSession session) {


        //fetch user from database given email and passhash
        User dbUser = new User();
        try {

            //convert the posted password into md5 hash
            String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();

            //fetch from database
            dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);

            //save the user in session
            session.setAttribute("currentUser", dbUser);
        } catch (Exception e) {

            throw new ApplicationException("no such user");
        }


        //update user last updated datetime
        userService.updateDtUpdated(dbUser.getId());



        //get session value
        String SESSION_VALUE = RequestContextHolder.currentRequestAttributes().getSessionId();

        //output successful json
        ApplicationResponse ar = new ApplicationResponse("ok", "signedin");
        ar.put("user_id", dbUser.getId().toString());
        ar.put("SESSION", SESSION_VALUE);

        return ar;
    }
}
