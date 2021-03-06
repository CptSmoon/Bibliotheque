package com.example.demo.models;

import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Vector;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer bookID;

    @NotNull
    @Size(min = 2, max = 100)
    private String bookTitle;

    @NotNull
    @Size(min = 4, max = 2000)
    private String bookDescription;


    @NotNull
    @Size(min = 2, max = 100)
    private String bookAuthor;


    private String bookPath;

    private String bookImage;


    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "book_category", joinColumns = @JoinColumn(name = "bookID"),
            inverseJoinColumns = @JoinColumn(name = "categoryID"))
    private List<Category> bookCategories;

    //Getters et setters

    public List<Category> getBookCategories() {
        return bookCategories;
    }

    public void setBookCategories(List<Category> bookCategories) {
        this.bookCategories = bookCategories;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    //Mes fonctions

    public String displayBookCategories(){
        int i;
        String s;
        s= bookCategories.get(0).getCategoryName();
        for (i=1; i<bookCategories.size();i++){
            s+=(","+bookCategories.get(i).getCategoryName());
        }
        return (s);
    }

    public Book(FormBook f,List<Category> c){
        setBookImage(f.getBookImage());
        setBookPath(f.getBookPath());
        setBookAuthor(f.getBookAuthor());
        setBookDescription(f.getBookDescription());
        setBookTitle(f.getBookTitle());
        setBookCategories(c);
    }
    public Book(){}
}
