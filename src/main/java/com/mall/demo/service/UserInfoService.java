package com.mall.demo.service;

import com.mall.demo.model.privilege.User;

import java.util.Optional;

public interface UserInfoService {
    /**通过username查找用户信息;*/
    public User findByAccount(String account);

    Iterable<User> findAll();

    Optional<User> getUserById(Long id);

    Long saveUser(User user);

    Long updateUser(User user);

    void deleteUserById(Long id);
}
