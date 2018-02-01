package com.example.demo.controller;

import com.example.demo.Services.UserService;
import com.example.demo.models.Book;
import com.example.demo.models.Category;
import com.example.demo.models.User;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String addForm(HttpSession session, Model model) {
        if(session.getAttribute("USER")==null){
            return "redirect:/login";
        };
        if(!(((User) session.getAttribute("USER"))
                .getUserRole().equals("ROLE_ADMIN"))) {
            return "redirect:/login";
        };

        model.addAttribute("cat", new Category());
        return "categoryAdder";
    }

    @PostMapping("/add")
    public ModelAndView addSubmit(HttpSession session, @Valid @ModelAttribute Category cat, BindingResult bindingResult) {
        if(session.getAttribute("USER")==null){
            return new ModelAndView("redirect:/login");
        };

        if (bindingResult.hasErrors()) {
            return new ModelAndView("categoryAdder");
        }
        if (categoryRepository.findAllByCategoryName(cat.getCategoryName()).size()==0){
            categoryRepository.save(cat);
        }
        return new ModelAndView("redirect:/category/all");
    }


    @GetMapping(path="/all")
    public String getAllCategories(HttpSession session, Model model) {
        if(session.getAttribute("USER")==null){
            return "redirect:/login";

        };
        if(!(((User) session.getAttribute("USER"))
                .getUserRole().equals("ROLE_ADMIN"))) {
            return "redirect:/login";
        };

        model.addAttribute("categories", categoryRepository.findAll());
        return "categoryLister";
    }

    @GetMapping("/edit/{id}")
    public String editForm(HttpSession session, @PathVariable("id")Integer id, Model model) {
        if(session.getAttribute("USER")==null){
            return "redirect:/login";
        };

        if(!(((User) session.getAttribute("USER"))
                .getUserRole().equals("ROLE_ADMIN"))) {
            return "redirect:/login";
        };
        Category c = categoryRepository.findOne(id);
        model.addAttribute("cat", c );
        return "categoryEdit";
    }

    @PostMapping("/edit")
    public ModelAndView editSubmit(HttpSession session, @ModelAttribute Category cat) {
        if(session.getAttribute("USER")==null){
            return new ModelAndView("redirect:/login");
        };
        categoryRepository.save(cat);
        return new ModelAndView("redirect:/category/all");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteCategory(HttpSession session, @PathVariable("id") Integer id, Model model) {
        if(session.getAttribute("USER")==null){
            return new ModelAndView("redirect:/login");
        };

        if(!(((User) session.getAttribute("USER"))
                .getUserRole().equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/login");
        };
        categoryRepository.delete(id);
        return new ModelAndView("redirect:/category/all");
    }
}
