package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(String username, String password, String displayName) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // **Lưu ý: Trong thực tế, bạn cần hash password**
        newUser.setDisplayName(displayName);
        return userRepository.save(newUser);
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) { // **Lưu ý: Trong thực tế, bạn cần so sánh password đã hash**
            return user;
        }
        return null;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateUserDisplayName(Long userId, String displayName) {
        User user = getUserById(userId);
        if (user != null) {
            user.setDisplayName(displayName);
            return userRepository.save(user);
        }
        return null;
    }
}