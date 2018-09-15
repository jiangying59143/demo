package com.mall.demo.service.impl;

import com.mall.demo.model.privilege.SysRole;
import com.mall.demo.model.privilege.User;
import com.mall.demo.repository.UserRepository;
import com.mall.demo.service.UserInfoService;
import com.mall.demo.common.utils.PasswordHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserRepository userRepository;

    @Override
    public User findByAccount(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername()");
        return userRepository.findByAccount(username);
    }

    @Override
    public User findByEmail(String email) {
        System.out.println("UserInfoServiceImpl.findByEmail()");
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByPhone(String phone) {
        System.out.println("UserInfoServiceImpl.findByPhone()");
        return userRepository.findByPhoneNumber(phone);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Long saveUser(User user) {
        PasswordHelper.encryptPassword(user);
        int index = new Random().nextInt(6) + 1;
        String avatar = "/static/user/user_" + index + ".png";
        user.setAvatar(avatar);
        user.setRoleList(Arrays.asList(new SysRole(2L)));
        return userRepository.save(user).getId();
    }


    @Override
    @Transactional
    public Long updateUser(User user) {
        Optional<User> oldUser = userRepository.findById(user.getId());
        oldUser.get().setNickname(user.getNickname());

        return oldUser.get().getId();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}