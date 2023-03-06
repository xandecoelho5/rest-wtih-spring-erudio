package com.xandecoelho5.restwithspringerudio.repository;

import com.xandecoelho5.restwithspringerudio.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
