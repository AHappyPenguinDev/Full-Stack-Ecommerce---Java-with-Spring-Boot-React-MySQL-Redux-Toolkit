package com.penguinshop.service;

import com.penguinshop.model.User;

public interface UserService {
    User findUserByJwtToken(String jwt) throws Exception;    
    User findUserByEmail(String email) throws Exception;
}
