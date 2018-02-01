package com.example.demo.Services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
@Autowired
    UserRepository userRepository;

    public User authenticateUser(String username, String password) {
        User u = userRepository.findByUserLogin(username);
        if (u==null)
            return null;
        if (!u.getUserPassword().equals(password))
            return null;
        return u;
    }

}
