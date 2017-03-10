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


    //gets a single thread
    public Thread getThread(Long threadId) {

        return threadRepository.getThread(threadId);
    }


    //gets all threads
    public List<Thread> getThreads() {

        return threadRepository.getThreads();
    }


    //gets all threads belonging to a particular customer
    public List<Thread> getThreadsByCustomerId(Long customerId) {

        return threadRepository.getThreadsByCustomerId(customerId);
    }


    //create a thread
    public Long createThread(String title, Long customerId) {

        return threadRepository.createThread(title, customerId);
    }
}
