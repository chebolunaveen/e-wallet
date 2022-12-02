package com;

import com.UserNotFoundException;
import com.UserRepository;
import com.UserRequest;
import com.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class UserService {
    public  final String REDIS_PREFIX_KEY ="user::" ;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    public void createuser(UserRequest userRequest){
        Users users= Users.builder().name(userRequest.getName()).age(userRequest.getAge()).userName(userRequest.getUserName()).
                mail(userRequest.getMail()).build();
        userRepository.save(users);
        saveIncache(users); //save in cache
        //send message through kafka
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("userName",users.getUserName());

        jsonObject.put("name",users.getName());
        String message=jsonObject.toString();
        kafkaTemplate.send("create_wallet",message);


    }
    public void saveIncache(Users users){
        Map map= objectMapper.convertValue(users,Map.class);
        redisTemplate.opsForHash().putAll(REDIS_PREFIX_KEY+users.getUserName(),map);
        redisTemplate.expire(REDIS_PREFIX_KEY+ users.getUserName(), Duration.ofHours(24));
    }
  public Users getUserbyName(String userName) throws Exception{

        Map map=redisTemplate.opsForHash().entries(REDIS_PREFIX_KEY+userName);
        if(map==null || map.size()==0) {
            try {
                Users users = userRepository.findByUserName(userName);
                if (users == null) {
                    throw new UserNotFoundException();
                }
                return users;
            } catch (Exception e) {
                throw new UserNotFoundException();
            }
        }else{
            Users object=objectMapper.convertValue(map,Users.class);
            return object;
        }

  }
}
