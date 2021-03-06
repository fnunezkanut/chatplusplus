/**
 * @file ChatSubmitController
 *
 */

package ie.nuigalway.sd3.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.nuigalway.sd3.ApplicationException;
import ie.nuigalway.sd3.ApplicationResponse;
import ie.nuigalway.sd3.entities.MessageView1;
import ie.nuigalway.sd3.entities.Thread;
import ie.nuigalway.sd3.entities.User;
import ie.nuigalway.sd3.services.MessageService;
import ie.nuigalway.sd3.services.ThreadService;
import ie.nuigalway.sd3.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ChatSubmitController {

    @Autowired
    private ThreadService threadService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;


    /**
     * inserts a new message into database, and responds with complete list of messages  in reverse order
     *
     * @param threadId
     * @param jsonRequest
     * @return
     */
    @MessageMapping("/thread/{threadId}")
    @SendTo("/topic/chat")
    public ApplicationResponse action(
            @DestinationVariable String threadId,
            String jsonRequest
    ) {


        //parse our incoming JSON string to Map
        Map<String, String> jsonMap = new HashMap<String, String>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            jsonMap = mapper.readValue(jsonRequest, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {

            return new ApplicationResponse("error", "Invalid input");
        }


        //fetch user from database given user_id
        User dbUser = new User();
        try {

            dbUser = userService.getUser(Long.parseLong(jsonMap.get("user_id")));
        } catch (Exception e) {

            return new ApplicationResponse("error", e.getMessage() );
        }


        //fetch this thread
        Thread dbThread = new Thread();
        try {

            dbThread = threadService.getThread(Long.parseLong(threadId));
        } catch (Exception e) {

            return new ApplicationResponse("error", e.getMessage() );
        }


        //check this user is allowed to respond to this thread (either customer who created it or customer support)
        if (dbThread.getCustomerId().equals(dbUser.getId())) {

            //all ok this thread belongs to this customer
        } else {

            //check if this user is a support person
            if (dbUser.getIsSupport() == true) {

                //all ok current user is a support person so allowed to respond
            } else {

                return new ApplicationResponse("error", "Not allowed to access this thread" );
            }
        }


        //only add a message if its larger than 1 character in length
        String message = jsonMap.get("message");
        boolean added = false;
        if (message.length() > 1) {

            //add new message to this thread
            messageService.addMessageToThread(dbThread.getId(), dbUser.getId(), message);

            //update thread last updated datetime
            threadService.updateDtUpdated( dbThread.getId() );

            added = true;
        }


        //fetch all the messages for this thread (in reverse order)
        List<MessageView1> messages = messageService.getMessagesByThreadId(dbThread.getId());



        //convert from a MessageView1 list to a hashmap using java8 streams
        Map<String, Object> messagesMap = messages.stream().collect(

                Collectors.toMap(x -> x.getId().toString(), x -> x)
        );


        //response message changes depending on if we are just retrieving messages or adding+ retrieving
        String responseMsg = "retrieved";
        if( added == true ){
            responseMsg = "added";
        }


        ApplicationResponse ar = new ApplicationResponse("ok", responseMsg );
        ar.setPayload( (HashMap<String, Object>) messagesMap );
        return ar;
    }
}
