package com.example.demo.validators;

import com.example.demo.repositories.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public class UniqueEmailValidator implements ConstraintValidator {
    public UserRepository userRepository;

    public UniqueEmailValidator(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    public void initialize(UniqueEmail constraint){

    }
    public boolean isValid(String email ,ConstraintValidatorContext context){
        return (email != null) && (userRepository.findByUserMail(email)!=null);

    }

    @Override
    public void initialize(Annotation annotation) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
