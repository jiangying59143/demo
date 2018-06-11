package com.mall.demo.dao;

import com.mall.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserNameOrEmail(String bb, String s);

    User findByUserName(String aa1);
}
