package com.example.demo.repositories;

import com.example.demo.models.Privilege;
import org.springframework.data.repository.CrudRepository;

public interface PrivilegeRepository extends CrudRepository<Privilege, String> {

    Privilege findByName(String name);
}
