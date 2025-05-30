package com.refresh.refresh.controller;

import com.refresh.refresh.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public List<UserDTO> getAllUsers() {
        // 예제 데이터 반환
        List<UserDTO> users = new ArrayList<>();
        UserDTO user = new UserDTO();
        user.setUserId(1);
        user.setEmail("example@example.com");
        user.setNickname("ExampleNickname");
        user.setGender("Male");
        user.setRole("USER");
        user.setPhoneNumber("010-1234-5678");
        user.setUserImage("example_image_url");
        user.setUserBan(false);
        user.setCreatedAt(LocalDateTime.now());
        users.add(user);
        return users;
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable int id) {
        // 예제 데이터 반환
        UserDTO user = new UserDTO();
        user.setUserId(id);
        user.setEmail("example@example.com");
        user.setNickname("ExampleNickname");
        user.setGender("Male");
        user.setRole("USER");
        user.setPhoneNumber("010-1234-5678");
        user.setUserImage("example_image_url");
        user.setUserBan(false);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        // 예제: 생성된 사용자 반환
        userDTO.setUserId(1); // 임의의 ID 설정
        userDTO.setCreatedAt(LocalDateTime.now()); // LocalDateTime 직접 설정
        return userDTO;
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        // 예제: 업데이트된 사용자 반환
        userDTO.setUserId(id);
        return userDTO;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        // 예제: 삭제 메시지 반환
        return "User with ID " + id + " has been deleted.";
    }
}
