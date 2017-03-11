package ie.nuigalway.sd3.controllers;

import ie.nuigalway.sd3.entities.Thread;
import ie.nuigalway.sd3.entities.User;
import ie.nuigalway.sd3.services.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ChatController {

    //from .properties
    @Value("${app.RANDOM}")
    String app_RANDOM;
    @Value("${app.BASE_URL}")
    String app_BASE_URL;


    @Autowired
    private ThreadService threadService;


    @RequestMapping(
            value = "/chat",
            produces = MediaType.TEXT_HTML_VALUE
    )
    public ModelAndView action(
            ModelMap model,
            HttpSession session,
            @RequestParam("threadId") String threadId
    ) {


        //get current user from session
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {

            //redirect if not signed in
            return new ModelAndView("redirect:" + app_BASE_URL + "login");
        } else {


            //fetch this thread from database
            Thread thread;
            try {

                thread = threadService.getThread(Long.parseLong(threadId));
            } catch (Exception e) { //TODO better exception catching here

                return new ModelAndView("_error", "errorMsg", e.getMessage());
            }


            //if the current user is not support person check if he/she is allowed to access this thread
            if (currentUser.getIsSupport() == false) {

                if (!thread.getCustomerId().equals(currentUser.getId())) {

                    return new ModelAndView("_error", "errorMsg", "You are not allowed to view this thread");
                }
            }


            //pass data to twig view
            model.addAttribute("app_RANDOM", app_RANDOM);
            model.addAttribute("app_BASE_URL", app_BASE_URL);
            model.addAttribute("user_id", currentUser.getId());
            model.addAttribute("user_email", currentUser.getEmail());
            model.addAttribute("user_is_support", currentUser.getIsSupport());
            model.addAttribute("thread_title", thread.getTitle());
            model.addAttribute("thread_id", thread.getId());


            //return view name
            return new ModelAndView("chat");
        }
    }
}
