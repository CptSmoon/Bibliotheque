package com.example.demo.controller;

import com.example.demo.models.Category;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private CategoryRepository categoryRepository;


    @GetMapping("/add")
    public String addForm(Model model) {
        System.out.println("add getter reached");
        model.addAttribute("cat", new Category());
        return "categoryAdder";
    }

    @PostMapping("/add")
    public ModelAndView addSubmit(@Valid @ModelAttribute Category cat, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("categoryAdder");
        }
        categoryRepository.save(cat);
        System.out.println("add setter reached");
        return new ModelAndView("redirect:/category/all");
    }


    @GetMapping(path="/all")
    public String getAllCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categoryLister";
    }
}
