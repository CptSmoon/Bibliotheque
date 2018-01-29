package com.example.demo;

import com.example.demo.models.Privilege;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.PrivilegeRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    public InitialDataLoader() {
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
        return;
        Privilege readPrivilege
        =createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
        =createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
        readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.setUserFirstName("Test");
        user.setUserLogin("Test");
        user.setUserLastName("Test");
        user.setUserPassword("test");
        user.setUserMail("test@test.com");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);
        alreadySetup = true;
        }

        @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
        privilege = new Privilege(name);
        privilegeRepository.save(privilege);
        }
        return privilege;
        }



    @Transactional
    public Role createRoleIfNotFound(
        String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
        role = new Role(name);
        role.setPrivileges(privileges);
        roleRepository.save(role);
        }
        return role;
        }
}