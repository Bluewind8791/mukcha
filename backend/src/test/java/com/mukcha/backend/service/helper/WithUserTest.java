package com.mukcha.backend.service.helper;

import com.mukcha.backend.repository.UserRepository;
import com.mukcha.backend.service.UserSecurityService;
import com.mukcha.backend.service.UserService;

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
