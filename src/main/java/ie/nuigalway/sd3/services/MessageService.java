package ie.nuigalway.sd3.services;


import ie.nuigalway.sd3.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    //a repository is chosen depending on which profile is run (dev or prod)

    //adding new message to a thread
    public Long addMessageToThread( Long threadId, Long userId, String comment ){

        return messageRepository.addMessageToThread( threadId, userId, comment );
    }

    //fetching all messages belonging to a thread
    public List<Map<String,Object>> getMessagesByThreadId( Long threadId ){

        return messageRepository.getMessagesByThreadId( threadId );
    }
}
