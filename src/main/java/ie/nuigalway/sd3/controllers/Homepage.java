package ie.nuigalway.sd3.controllers;

import ie.nuigalway.sd3.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class Homepage {

    //from .properties
    @Value("${app.RANDOM}")
    String app_RANDOM;
    @Value("${app.BASE_URL}")
    String app_BASE_URL;

    @RequestMapping("/")
    public ModelAndView action(ModelMap model, HttpSession session) {


        //get current user from session
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {

            //pass data to twig view
            model.addAttribute("app_RANDOM", app_RANDOM);
            model.addAttribute("app_BASE_URL", app_BASE_URL);

            //show homepage
            return new ModelAndView("homepage");
        } else {


            //redirect if user is signed in to support page
            return new ModelAndView("redirect:" + app_BASE_URL + "support");
        }
    }
}
