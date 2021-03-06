package com.mall.demo.repository;

import com.mall.demo.model.privilege.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    /**通过username查找用户信息;*/
    public User findByAccount(String account);

    public User findByEmailAndState(String email, byte state);

    public User findByEmail(String email);

    public User findByOpenIdAndThirdType(String openId, String type);

    public User findByPhoneNumber(String phone);

}
