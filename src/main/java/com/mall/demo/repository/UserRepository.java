package com.mall.demo.repository;

import com.mall.demo.model.privilege.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    /**通过username查找用户信息;*/
    public User findByAccount(String account);


}
