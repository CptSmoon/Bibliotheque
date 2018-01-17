package com.example.demo.repositories;
import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.Book;

public interface bookRepository extends CrudRepository<Book, Integer> {

}