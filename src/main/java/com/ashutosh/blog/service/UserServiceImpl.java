package com.ashutosh.blog.service;

import com.ashutosh.blog.dao.UserRepository;
import com.ashutosh.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository theUserRepository){
        userRepository= theUserRepository;
    }
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(int id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
