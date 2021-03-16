package com.geektimes.projects.user.management;

import com.geektimes.projects.user.domain.User;

public class UserManager implements UserManagerMBean {

    private final User user;

    public UserManager(User user){
        this.user=user;
    }

    @Override
    public Long getId() {
        return user.getId();
    }

    @Override
    public void setId(Long id) {
        this.user.setId(id);
    }

    @Override
    public String getName() {
        return this.user.getName();
    }

    @Override
    public void setName(String name) {
        this.user.setName(name);
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public void setPassword(String password) {
        this.user.setPassword(password);
    }

    @Override
    public String getEmail() {
        return this.user.getEmail();
    }

    @Override
    public void setEmail(String email) {
        this.user.setEmail(email);
    }

    @Override
    public String getPhoneNumber() {
        return this.user.getPhoneNumber();
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.user.setPhoneNumber(phoneNumber);
    }
}
