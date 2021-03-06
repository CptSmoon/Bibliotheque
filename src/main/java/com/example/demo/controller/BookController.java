package com.example.demo.controller;

import com.example.demo.models.Book;
import com.example.demo.models.Category;
import com.example.demo.models.FormBook;
import com.example.demo.models.User;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.CategoryRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookRepository BookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Pattern patternImage;
    private Pattern patternBook;
    private Matcher matcherImage;
    private Matcher matcherBook;


    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    private static final String BOOK_PATTERN = "([^\\s]+(\\.(?i)(pdf))$)";

    private static String IMAGE_UPLOAD_FOLDER = "src/main/resources/static/img/";
    private static String PATH_UPLOAD_FOLDER = "src/main/resources/static/path/";
    private static long MAX_FILE_SIZE = 1000000000;
    private static long MAX_IMAGE_SIZE = 2000000000;

    BookController(){
        patternBook = Pattern.compile(BOOK_PATTERN);
        patternImage = Pattern.compile(IMAGE_PATTERN);}

    @GetMapping("/add")
    public String addForm(HttpSession session, ModelMap model) {
        if(session.getAttribute("USER")==null){
            return "redirect:/login";

        };

        if(!(((User) session.getAttribute("USER"))
                .getUserRole().equals("ROLE_ADMIN"))) {
            return "redirect:/login";
        };

        model.addAttribute("bookForm", new FormBook());
        model.addAttribute("categories", categoryRepository.findAll());
        Vector <Integer> V = new Vector<Integer>();
        V.add(1);
        V.add(12);
        V.add(13);
        model.addAttribute("categoryIDs",V);
        return "adder";
    }

    @PostMapping("/add")
    public ModelAndView addSubmit(HttpSession session,
            @ModelAttribute("bookForm") FormBook bookForm,
            BindingResult bindingResult,
            @RequestParam("image") MultipartFile image,
            @RequestParam("file") MultipartFile file, ModelMap model) {

        if(session.getAttribute("USER")==null){
            return new ModelAndView("redirect:/login");

        };

        if(!(((User) session.getAttribute("USER"))
                .getUserRole().equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/login");
        };
        List<Category> categories = new Vector<Category>();
        for(Integer i : bookForm.getCategoriesIDs()){
            Category c = categoryRepository.findByCategoryID(i);
            categories.add(c);
        }
        Book book = new Book(bookForm,categories);
        if (!file.isEmpty()) {
            try{

                if(file.getOriginalFilename().length()<=4){
                    bindingResult.rejectValue("bookPath", "fichier.errone", "size is less");
                }
                else if(!file.getOriginalFilename().substring(file.
                        getOriginalFilename().length() - 4).equals(".pdf"))
                {
                    bindingResult.rejectValue("bookPath", "fichier.errone", "Wrong file format");
                }
                else if(file.getSize()>MAX_FILE_SIZE){
                    bindingResult.rejectValue("bookPath", "fichier.taille", "File limit exceeded MAX : "+MAX_FILE_SIZE);
                }

            }
            catch(NullPointerException Ex){
                Ex.printStackTrace();
            }

            try {
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                String ran1 = RandomStringUtils.randomAlphabetic(20);

                Path path1 = Paths.get(PATH_UPLOAD_FOLDER +ran1+".pdf");

                Files.write(path1, bytes);
                book.setBookPath(ran1+".pdf");

            } catch (IOException e) {
                book.setBookPath("no path");
            }
        }
        else {
            bindingResult.rejectValue("bookPath", "fichier.inexistant", "Choose a pdf file please");
        }

        if (!image.isEmpty()) {

            try{
                matcherImage = patternImage.matcher(image.getOriginalFilename());
                if (!matcherImage.matches()) {
                    bindingResult.rejectValue("bookImage", "image.erronee", "Wrong image format");
                }
                else if(file.getSize()>MAX_IMAGE_SIZE){
                    bindingResult.rejectValue("bookImage", "image.taille", "Image limit exceeded MAX : "+MAX_IMAGE_SIZE);
                } }
            catch(NullPointerException Ex){
                Ex.printStackTrace();
            }
            try {
                // Get the image and save it somewhere
                byte[] bytes1 = image.getBytes();
                String ran2 = RandomStringUtils.randomAlphabetic(6);
                String name2 = ran2 + "_" + image.getOriginalFilename();
                Path path2 = Paths.get(IMAGE_UPLOAD_FOLDER +  name2);
                Files.write(path2, bytes1);
                book.setBookImage(name2);
            } catch (IOException e) {
                book.setBookImage("alt.png");
            }
        } else {
            bindingResult.rejectValue("bookImage", "image.inexistante", "Choose an imange please");

        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());

            return new ModelAndView("adder");
        }
        BookRepository.save(book);
        return new ModelAndView("redirect:/book/all");
    }

    @GetMapping(path = "/all")
    public String getAllBooks(HttpSession session,Model model) {
        if(session.getAttribute("USER")==null){
            return "redirect:/login";
        };
        if(!(((User) session.getAttribute("USER"))
                .getUserRole().equals("ROLE_ADMIN"))) {
            return "redirect:/login";
        };
        model.addAttribute("books", BookRepository.findAll());
        return "lister";
    }

    @GetMapping("/edit/{id}")
    public String editForm(HttpSession session,@PathVariable("id") Integer id, Model model) {
        if(session.getAttribute("USER")==null){
            return "redirect:/login";
        };
        if(!(((User) session.getAttribute("USER"))
                .getUserRole().equals("ROLE_ADMIN"))) {
            return "redirect:/login";
        };
        Book b = BookRepository.findOne(id);
        model.addAttribute("book", b);
        return "edit";
    }

    @GetMapping("/details/{id}")
    public String detailsBook(HttpSession session,@PathVariable("id") Integer id, Model model) {
        if(session.getAttribute("USER")==null){
            return "redirect:/login";
        };

        Book b = BookRepository.findOne(id);
        model.addAttribute("book", b);
        return "bookDetail";
    }

    @PostMapping("/edit")

    public ModelAndView editSubmit(HttpSession session,@ModelAttribute Book book) {
        if(session.getAttribute("USER")==null){
            return new ModelAndView("redirect:/login");
        };
        if(!(((User) session.getAttribute("USER"))
                .getUserRole().equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/login");
        };
        BookRepository.save(book);
        return new ModelAndView("redirect:/book/all");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteBook(HttpSession session,@PathVariable("id") Integer id, Model model) {
        if(session.getAttribute("USER")==null){
            return new ModelAndView("redirect:/login");
        };

        if(!(((User) session.getAttribute("USER"))
                .getUserRole().equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/login");
        };
        Book b =BookRepository.findOne(id);

        File filePDF = FileUtils.getFile("src/test/resources/static/path/"+b.getBookPath());
        File fileIMG = FileUtils.getFile("src/test/resources/static/img/"+b.getBookImage());
        FileUtils.deleteQuietly(filePDF);
        FileUtils.deleteQuietly(fileIMG);

        BookRepository.delete(b);
        return new ModelAndView("redirect:/book/all");
    }

}
