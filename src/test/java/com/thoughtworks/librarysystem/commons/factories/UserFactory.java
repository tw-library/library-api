package com.thoughtworks.librarysystem.commons.factories;

/**
 * Created by eferreir on 2/16/16.
 */

import com.thoughtworks.librarysystem.commons.ApplicationTestBase;
import com.thoughtworks.librarysystem.user.User;
import com.thoughtworks.librarysystem.user.UserBuilder;


public class UserFactory extends ApplicationTestBase {

    public User createDefaultUser() {
        User user = new UserBuilder()
                .withEmail("userdefault@thoughtworks.com")
                .withName("User Default")
                .build();

        return user;
    }

    public User createUserWithUserName(String username) {
        User user = new UserBuilder()
                .withEmail(username + "@thoughtworks.com")
                .withName(username)
                .build();

        return user;
    }
}
