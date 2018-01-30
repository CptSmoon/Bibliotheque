package com.example.demo.models;

import java.util.List;
import java.util.Vector;

public class FormBook
{
    private Integer bookID;
    private String bookTitle;
    private String bookDescription;
    private String bookAuthor;
    private String bookPath;
    private String bookImage;
    private List<Integer> categoriesIDs;

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
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

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public List<Integer> getCategoriesIDs() {
        return categoriesIDs;
    }

    public void setCategoriesIDs(List<Integer> categoriesIDs) {
        this.categoriesIDs = categoriesIDs;
    }
}
