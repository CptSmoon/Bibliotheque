package com.example.demo.controller;

import com.example.demo.models.Book;
import com.example.demo.repositories.bookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookController {
    @Autowired
    // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private bookRepository bookRepository;

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("book", new Book());
        return "adder";
    }

    @PostMapping("/add")
    public  ModelAndView addSubmit(@ModelAttribute Book book) {
        bookRepository.save(book);

        return new ModelAndView("redirect:/all");
    }

    @GetMapping(path="/all")
    public String getAllUsers(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "lister";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id")Integer id,Model model) {
        Book b = bookRepository.findOne(id);
        System.out.println(b);
        model.addAttribute("book", b);
        return "edit";
    }

    @PostMapping("/edit")
    public  @ResponseBody String editSubmit(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "edit";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id")Integer id,Model model) {
        bookRepository.delete(id);
        return "lister" ;
    }

}
