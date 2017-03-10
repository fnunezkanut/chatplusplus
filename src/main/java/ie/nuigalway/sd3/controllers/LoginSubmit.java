package ie.nuigalway.sd3.controllers;

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

import javax.servlet.http.HttpSession;

@RestController
public class LoginSubmit {


    @Autowired
    private UserService userService;


    //receives email+pass via ajax POST
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/login/submit",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ApplicationResponse action(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "pass") String pass,
            HttpSession session
    ) {


        //convert the posted password into md5 hash
        String passwordHash = DigestUtils.md5Hex(pass).toUpperCase();

        //fetch user from database given email and passhash
        User dbUser = new User();
        try {

            dbUser = userService.getUserByEmailAndPasshash(email, passwordHash);

            //save the user in session
            session.setAttribute("currentUser", dbUser);
        } catch (Exception e) { //TODO better exception catching here

            return new ApplicationResponse("error", e.getMessage());
        }


        //output successful json
        ApplicationResponse jsonResponse = new ApplicationResponse("ok", "signedin");
        jsonResponse.put("user_id", dbUser.getId().toString());
        return jsonResponse;
    }
}
