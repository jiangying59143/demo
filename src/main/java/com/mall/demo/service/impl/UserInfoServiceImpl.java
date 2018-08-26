package com.mall.demo.service.impl;

import com.mall.demo.repository.UserRepository;
import com.mall.demo.model.privilege.UserInfo;
import com.mall.demo.service.UserInfoService;
import com.mall.demo.util.PasswordHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Random;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserRepository userRepository;

    @Override
    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername()");
        return userRepository.findByUsername(username);
    }

    @Override
    public Iterable<UserInfo> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserInfo> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Long saveUser(UserInfo user) {

        PasswordHelper.encryptPassword(user);
        int index = new Random().nextInt(6) + 1;
        String avatar = "/static/user/user_" + index + ".png";

        user.setAvatar(avatar);
        return userRepository.save(user).getId();
    }


    @Override
    @Transactional
    public Long updateUser(UserInfo user) {
        Optional<UserInfo> oldUser = userRepository.findById(user.getId());
        oldUser.get().setName(user.getName());

        return oldUser.get().getId();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}