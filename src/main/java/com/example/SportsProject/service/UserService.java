package com.example.SportsProject.service;

import com.example.SportsProject.dto.UserLoginDTO;
import com.example.SportsProject.entity.User;
import com.example.SportsProject.repository.UserRepository;
import jakarta.validation.constraints.Null;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean signIn(UserLoginDTO userLoginDTO) {
        User userCheck = userRepository.findUserByEmail(userLoginDTO.getEmail());
        if (userCheck == null) {
            return false;
        }

        return userLoginDTO.getPassword().equals(userCheck.getPassword());
    }

    public String signUp(User user) {
        User userCheck = userRepository.findUserByEmail(user.getEmail());

        if (userCheck == null){
            user.setRole("USER");
            userRepository.save(user);
            return "User saved";
        }

        return "User already exists";
    }
}
