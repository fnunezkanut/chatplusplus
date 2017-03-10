package ie.nuigalway.sd3.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;


@Controller
public class Logout {

    //from .properties
    @Value("${app.RANDOM}")
    String app_RANDOM;
    @Value("${app.BASE_URL}")
    String app_BASE_URL;


    //shows login page
    @RequestMapping(
            value = "/logout",
            produces = MediaType.TEXT_HTML_VALUE
    )
    public ModelAndView action(
            ModelMap model,
            HttpSession session
    ) {

        //remove the current user from session
        session.setAttribute("currentUser", null);


        session.invalidate();


        //pass data to twig view
        model.addAttribute("app_RANDOM", app_RANDOM);
        model.addAttribute("app_BASE_URL", app_BASE_URL);

        //return view name
        return new ModelAndView("logout");
    }
}
