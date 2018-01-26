package com.example.demo.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Book {

    //Attributs

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer bookID;

    @NotNull
    private String bookTitle;

    private String bookDescription;

    @NotNull
    private String bookAuthor;

    @NotNull
    private String bookPath;

    private String bookImage;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_category", joinColumns = @JoinColumn(name = "bookID"), inverseJoinColumns = @JoinColumn(name = "categoryName"))
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

    public void displayBookCategories(){
        int i;
        for (i=0; i<bookCategories.size();i++){
            System.out.println(bookCategories.get(i));
        }
    }
}
