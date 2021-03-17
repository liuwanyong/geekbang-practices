package com.geektimes.projects.user.service.impl;

import com.geektimes.projects.user.domain.User;
import com.geektimes.projects.user.repository.UserRepository;
import com.geektimes.projects.user.service.UserService;

public class UserServiceImpl implements UserService {

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    private UserRepository userRepository;

    @Override
    public boolean register(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean deregister(User user) {
        return userRepository.deleteById(user.getId());
    }

    @Override
    public boolean update(User user) {
        return userRepository.update(user);
    }

    @Override
    public User queryUserById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return userRepository.getByNameAndPassword(name,password);
    }
}
