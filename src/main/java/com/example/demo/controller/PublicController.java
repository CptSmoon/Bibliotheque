package com.example.demo.controller;

import com.example.demo.Services.UserService;
import com.example.demo.models.FormUser;
import com.example.demo.models.User;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.UserRepository;
import org.jcp.xml.dsig.internal.dom.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping(value={"/index","/","/home","/welcome"})
public class PublicController {
    @Autowired
    UserService userService;
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
    public String loginGet(HttpSession session, ModelMap model)
    {
        model.addAttribute("formUser", new FormUser());
        return "login";
    }

    @PostMapping("/loginp")
    public ModelAndView loginAdd( HttpSession session,@Valid @ModelAttribute FormUser formUser,
                                 BindingResult bindingResult)
    {
        User user=userService.authenticateUser(formUser.getUserLogin(), formUser.getUserPassword());
        if(user!=null) {
            session.setAttribute("USER", user);
        } else {
            System.out.println("Hello2");

            bindingResult.rejectValue("userLogin", "compte.erroné", "Authentification incorrecte");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("userAdder");
        }
        System.out.println("Hello3");

        return new ModelAndView("redirect:/index");
    }


    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        return "accessDenied";
    }




}
