package com.ifortex.bookservice.repository;

import com.ifortex.bookservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {


}