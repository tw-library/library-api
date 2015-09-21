package com.thoughtworks.librarysystem.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

@AllArgsConstructor
@RequiredArgsConstructor
@Wither
public class UserBuilder {

    private Integer id;
    private String email;
    private String name;

    public User build(){

        User user = new User();

        user.setEmail(email);
        user.setId(id);
        user.setName(name);

        return user;
    }
}
