package com.nice.service.impl;

import com.nice.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl  implements UserService {
    @Override
    public void addUser(String userName, String password) {
        System.out.println("userName = " + userName +", password = " + password);
    }
}
