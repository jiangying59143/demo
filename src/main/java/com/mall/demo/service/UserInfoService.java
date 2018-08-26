package com.mall.demo.service;

import com.mall.demo.model.privilege.UserInfo;

import java.util.Optional;

public interface UserInfoService {
    /**通过username查找用户信息;*/
    public UserInfo findByUsername(String username);

    Iterable<UserInfo> findAll();

    Optional<UserInfo> getUserById(Long id);

    Long saveUser(UserInfo user);

    Long updateUser(UserInfo user);

    void deleteUserById(Long id);
}
