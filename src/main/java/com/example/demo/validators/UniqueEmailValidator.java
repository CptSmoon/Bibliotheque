package com.example.demo.validators;

import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public class UniqueEmailValidator implements ConstraintValidator  <UniqueEmail,String>{


    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void initialize(UniqueEmail constraint){

    }
    @Override
    public boolean isValid(String email ,ConstraintValidatorContext context){
//        return (email != null) && (userRepository.findAllByUserMail(email)!=null);
        return (email != null) && (email.equals("a@a.aa"));

    }





}
