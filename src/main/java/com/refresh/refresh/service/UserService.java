package com.refresh.refresh.service;

import com.refresh.refresh.entity.User;
import com.refresh.refresh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll(); // 엔티티 User 사용
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")); // 엔티티 User 사용
    }

    public User createUser(User user) {
        return userRepository.save(user); // 엔티티 User 사용
    }

    public User updateUser(User user) {
        if (!userRepository.existsById(user.getUserId())) {
            throw new RuntimeException("User not found");
        }
        return userRepository.save(user); // 엔티티 User 사용
    }

    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id); // 엔티티 User 사용
    }
}
