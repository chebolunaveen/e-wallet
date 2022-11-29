import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")

public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("/add")
    public void CreateUser(@RequestBody() UserRequest userRequest){
        userService.createuser(userRequest);
    }
    @GetMapping("/userName")
       public User getUserbyName(@RequestParam("userName") String userName) throws Exception{
        return userService.getUserbyName(userName);
    }
}
