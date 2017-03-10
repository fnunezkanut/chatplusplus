package ie.nuigalway.sd3.services;


import ie.nuigalway.sd3.entities.Thread;
import ie.nuigalway.sd3.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ThreadService {

    @Autowired
    private ThreadRepository threadRepository;
    //a repository is chosen depending on which profile is run (dev or prod)

    public List<Thread> getThreads(){

        return threadRepository.getThreads();
    }

    public Thread getThread(Long threadId){

        return threadRepository.getThread( threadId );
    }

    public Long createThread( String title, long customerId ){

        return threadRepository.createThread( title, customerId );
    }

    public void updateDtUpdated( Long threadId ){

        threadRepository.updateDtUpdated( threadId );
    }
}
