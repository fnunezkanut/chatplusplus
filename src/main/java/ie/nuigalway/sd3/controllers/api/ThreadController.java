package ie.nuigalway.sd3.controllers.api;

import ie.nuigalway.sd3.ApplicationException;
import ie.nuigalway.sd3.ApplicationResponse;
import ie.nuigalway.sd3.entities.Thread;
import ie.nuigalway.sd3.entities.User;
import ie.nuigalway.sd3.services.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ThreadController {

    @Autowired
    private ThreadService threadService;


    //creating a thread with a title and a user_id (extracted from session)
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/api/v1/threads",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ApplicationResponse createThread(
            HttpSession session,
            @RequestParam("title") String title
    ) {

        //check current user is signed in
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {

            return new ApplicationResponse("error", "Current user is not signed in");
        }


        //create a new thread
        Long newThreadId;
        try {

            newThreadId = threadService.createThread(title, currentUser.getId());
        } catch (Exception e) {

            return new ApplicationResponse("error", e.getMessage());
        }


        //output successful json
        ApplicationResponse jsonResponse = new ApplicationResponse("ok", "created");
        jsonResponse.put("thread_id", Long.toString(newThreadId));
        return jsonResponse;

    }


    //fetching all threads
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/v1/threads"
    )
    public ResponseEntity getThreads(HttpSession session) {

		/*
        //check current user is signed in
		User currentUser = (User) session.getAttribute( "currentUser" );
		if ( currentUser == null ) {

			return new JsonResponse( "error", "Current user is not signed in" );
		}


		//check current user is support person
		if ( currentUser.getIsSupport() == false ) {

			return new JsonResponse( "error", "Current user is not a support person" );
		}
		*/


        List<Thread> threads;
        try {

            threads = threadService.getThreads();
        } catch (Exception e) {

            return null;
            //return new JsonResponse( "error", "Unable to fetch threads" );
        }


        throw new ApplicationException("test");


        //return new ResponseEntity<List<Thread>>( threads, HttpStatus.OK);
    }

}
