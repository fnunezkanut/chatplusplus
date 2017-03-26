package ie.nuigalway.sd3.services;


import ie.nuigalway.sd3.entities.User;
import ie.nuigalway.sd3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    //a repository is chosen depending on which profile is run (dev or prod)


    public User getUser(Long userId) {

        return userRepository.getUser(userId);
    }

    public User getUserByEmailAndPasshash(String email, String passhash) {

        return userRepository.getUserByEmailAndPasshash(email, passhash);
    }

    public void updateDtUpdated(Long userId) {

        userRepository.updateDtUpdated(userId);
    }
}
