package com.mall.demo.repository;

import com.mall.demo.model.sys.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer> {
}
