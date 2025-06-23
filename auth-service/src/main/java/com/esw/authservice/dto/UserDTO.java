package com.esw.authservice.dto;

import com.esw.authservice.model.User;

public class UserDTO {
    private String username;
    private String email;

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
