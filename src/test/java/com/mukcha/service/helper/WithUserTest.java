package com.mukcha.service.helper;

import com.mukcha.repository.UserRepository;
import com.mukcha.service.UserSecurityService;
import com.mukcha.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;


public class WithUserTest {
    
    @Autowired protected UserRepository userRepository;
    
    protected UserService userService;
    protected UserTestHelper userTestHelper;
    protected UserSecurityService userSecurityService;

    protected void prepareUserServiceTest() {

        // clear repo
        this.userRepository.deleteAll();

        // service
        this.userService = new UserService(userRepository);
        this.userSecurityService = new UserSecurityService(userRepository);
        
        //test helper
        this.userTestHelper = new UserTestHelper(userService, NoOpPasswordEncoder.getInstance());
    }
}
