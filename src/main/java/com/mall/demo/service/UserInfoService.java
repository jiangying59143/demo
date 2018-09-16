package com.mall.demo.service;

import com.mall.demo.model.privilege.User;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

public interface UserInfoService {
    /**通过username查找用户信息;*/
    public User findByAccount(String account);

    public User findByEmailAndState(String email, byte stat);

    public User findByEmail(String email);

    public User findByPhone(String phone);

    public User findByOpenIdAndThirdType(String openId, String type);

    Iterable<User> findAll();

    @Cacheable(value="andCache",key="#id + 'getUserById'")
    Optional<User> getUserById(Long id);

    Long saveUser(User user);

    Long updateUser(User user);

    void deleteUserById(Long id);
}
