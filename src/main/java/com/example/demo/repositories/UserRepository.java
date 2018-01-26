package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findAllByUserMail(String email);
}