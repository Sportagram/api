package com.BDiary.webDemo.service;

import com.BDiary.webDemo.entity.user;
import com.BDiary.webDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public user findUser(String googlename) {
        return userRepository.findByGooglename(googlename);
    }

    public user saveUser(user user) {
        return userRepository.save(user);
    }
}