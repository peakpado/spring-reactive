package com.example.springreactive;

import com.example.springreactive.data.User;
import com.example.springreactive.data.UserRepository;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartRunner implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(AppStartRunner.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String...args) throws Exception {
        LOG.info("Starting app --------------");
//        User user = new User();
//        user.setFirstname("Spring");
//        user.setLastname("Last");
//        userRepository.save(user);
    }
}