package com.example.demo.controller;

import com.example.demo.models.User;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value={"/","/home","/welcome"})
public class PublicController {
    @Autowired
    BookRepository bookRepository;
//    @GetMapping("/error")
//    public String getError(ModelMap model){return "error";}
    @GetMapping("")
    public String getHome(ModelMap model){

        model.addAttribute("books", bookRepository.findAll());
        return "index";
    }

    @GetMapping("/login")
    public String loginGet(ModelMap model) {
        return "login";
    }

    @PostMapping("/login")
    public String loginAdd(ModelMap model) {
        return "index";
    }

    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        return "accessDenied";
    }




}
