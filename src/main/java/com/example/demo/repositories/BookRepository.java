package com.example.demo.repositories;
import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {

}