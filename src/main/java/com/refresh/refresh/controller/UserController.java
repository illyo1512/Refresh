package com.refresh.refresh.controller;

import com.refresh.refresh.dto.UserDTO;
import com.refresh.refresh.entity.User;
import com.refresh.refresh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return convertToDTO(user);
    }

    @PostMapping //사용자 생성
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        User user = new User(); // 엔티티 User 사용
        user.setEmail(userDTO.getEmail());
        user.setNickname(userDTO.getNickname());
        user.setGender(userDTO.getGender());
        user.setRole(userDTO.getRole());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUserImage(userDTO.getUserImage());
        user.setPassword(userDTO.getPassword());
        user.setUserBan(userDTO.getUserBan());
        user.setCreatedAt(userDTO.getCreatedAt());
        User savedUser = userService.createUser(user); // 엔티티 User 사용
        userDTO.setUserId(savedUser.getUserId());
        return userDTO;
    }

    @PutMapping("/{id}") // 사용자 정보 수정 (관리자용)
    public UserDTO updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        User user = new User();
        user.setUserId(id);
        user.setEmail(userDTO.getEmail());
        user.setNickname(userDTO.getNickname());
        user.setGender(userDTO.getGender());
        user.setRole(userDTO.getRole());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUserImage(userDTO.getUserImage());
        user.setUserBan(userDTO.getUserBan());

        User updatedUser = userService.updateUser(user);
        return convertToDTO(updatedUser);
    }

    @DeleteMapping("/{id}") // 사용자 삭제 (관리자용)
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "User with ID " + id + " has been deleted.";
    }

    // User를 UserDTO로 변환하는 헬퍼 메소드
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setEmail(user.getEmail());
        userDTO.setNickname(user.getNickname());
        userDTO.setGender(user.getGender());
        userDTO.setRole(user.getRole());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setUserImage(user.getUserImage());
        userDTO.setUserBan(user.getUserBan());
        userDTO.setCreatedAt(user.getCreatedAt());
        return userDTO;
    }
}
