package com.penguinshop.impl;

import org.springframework.stereotype.Service;

import com.penguinshop.config.JwtProvider;
import com.penguinshop.model.User;
import com.penguinshop.repository.UserRepository;
import com.penguinshop.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);

        User user = this.findUserByEmail(email);

        if (user == null) 
            throw new Exception("User not found with email: ");
        
        return user;
    }

    @Override
	public User findUserByEmail(String email) throws Exception {
		return userRepository.findByEmail(email);
	}

}
