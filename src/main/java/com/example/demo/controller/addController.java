package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class addController {

    @RequestMapping("/add")
    public String greetingForm(Model model) {

        System.out.println("HELLO");
        return "adder";
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
        public String greetingSubmit() {
        return "result";
    }
}
