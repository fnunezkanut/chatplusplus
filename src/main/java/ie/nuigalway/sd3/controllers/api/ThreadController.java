package ie.nuigalway.sd3.controllers.api;

import ie.nuigalway.sd3.entities.JsonResponse;
import ie.nuigalway.sd3.entities.Thread;
import ie.nuigalway.sd3.entities.User;
import ie.nuigalway.sd3.services.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@RestController
public class ThreadController {

	@Autowired
	private ThreadService threadService;



	//creating a thread with a title and a user_id (extracted from session)
	@RequestMapping(
			method=RequestMethod.POST,
			value="/api/v1/threads",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE
	)
	public JsonResponse createThread(
			HttpSession session,
			@RequestParam("title") String title
	                                ){

		User currentUser = (User)session.getAttribute( "currentUser" );
		if( currentUser == null ){

			return new JsonResponse("error", "Current user is not signed in" );
		}
		else{

			//create a new thread
			Long newThreadId;
			try{

				newThreadId = threadService.createThread( title, currentUser.getId() );
			}
			catch (Exception e){

				return new JsonResponse("error", e.getMessage() );
			}


			//output successful json
			JsonResponse jsonResponse = new JsonResponse("ok", "created" );
			jsonResponse.put( "thread_id", Long.toString( newThreadId ) );
			return jsonResponse;
		}
	}





	//fetching all threads
	@RequestMapping(method= RequestMethod.GET, value="/api/v1/threads")
	public List<Thread> getThreads(){

		return threadService.getThreads();
	}

	//fetching a particular thread
	@RequestMapping(method= RequestMethod.GET, value = "/api/v1/threads/{threadId}")
	public Thread getTopic( @PathVariable("threadId") Long threadId ){

		return threadService.getThread( threadId );
	}


	//update the threads timestamp
	@RequestMapping(method=RequestMethod.POST, value="/api/v1/threads/{threadId}/updated")
	public void updateDtUpdated( @PathVariable("threadId") Long threadId ){

		threadService.updateDtUpdated( threadId );
	}
}
