package com.kingdomeprotocol.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kingdomeprotocol.model.UserModel;

import jakarta.persistence.LockModeType;


public interface UserRepository extends JpaRepository<UserModel, Integer>{
Optional<UserModel> findByEmail(String email);
//Lock pessimistic user
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query(value = "select user from UserModel user where user.email = :email")
Optional<UserModel> findUser4Upd(@Param("email") String email);
}
