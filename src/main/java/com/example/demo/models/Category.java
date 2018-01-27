package com.example.demo.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Category {

    //Attributs

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer categoryID;


    @NotNull
    private String categoryName;

    @ManyToMany(mappedBy = "bookCategories")
    private List<Book> categoryBooks;

    //Getters et setters


    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;

    }


    public List<Book> getCategoryBooks() {
        return categoryBooks;
    }

    public void setCategoryBooks(List<Book> categoryBooks) {
        this.categoryBooks = categoryBooks;
    }

}
