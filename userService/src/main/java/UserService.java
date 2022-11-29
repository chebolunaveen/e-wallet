import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public void createuser(UserRequest userRequest){
        User user=User.builder().name(userRequest.getName()).age(userRequest.getAge()).userName(userRequest.getUserName()).
                mail(userRequest.getMail()).build();
        userRepository.save(user);
    }
  public User getUserbyName(String userName) throws Exception{

        try {
            User user=userRepository.findByUserName(userName);
            if (user == null) {
                throw new UserNotFoundException();
            }
            return user;
        }
        catch (Exception e){
            throw new UserNotFoundException();
        }

  }
}
