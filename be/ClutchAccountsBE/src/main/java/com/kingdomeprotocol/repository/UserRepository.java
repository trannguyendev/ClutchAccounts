package com.kingdomeprotocol.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingdomeprotocol.model.UserModel;


public interface UserRepository extends JpaRepository<UserModel, Integer>{
Optional<UserModel> findByEmail(String email);
}
