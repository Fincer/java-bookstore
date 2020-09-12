//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface BookRepository extends CrudRepository<Book, Long> {

	// Handles both INSERT and UPDATE queries
    List<Book> findById(String title);

}