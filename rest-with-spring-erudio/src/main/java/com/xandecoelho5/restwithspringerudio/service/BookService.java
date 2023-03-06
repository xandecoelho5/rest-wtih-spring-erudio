package com.xandecoelho5.restwithspringerudio.service;

import com.xandecoelho5.restwithspringerudio.controller.BookController;
import com.xandecoelho5.restwithspringerudio.data.vo.v1.BookVO;
import com.xandecoelho5.restwithspringerudio.exception.RequiredObjectIsNullException;
import com.xandecoelho5.restwithspringerudio.exception.ResourceNotFoundException;
import com.xandecoelho5.restwithspringerudio.model.Book;
import com.xandecoelho5.restwithspringerudio.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xandecoelho5.restwithspringerudio.mapper.CustomModelMapper.parseListObjects;
import static com.xandecoelho5.restwithspringerudio.mapper.CustomModelMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public BookVO create(BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        var entity = parseObject(book, Book.class);
        var vo = parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public List<BookVO> findAll() {
        var books = parseListObjects(repository.findAll(), BookVO.class);
        books.forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        return books;
    }

    public BookVO findById(Long id) {
        var vo = parseObject(getById(id), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        Book entity = getById(book.getKey());

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var vo = parseObject(getById(book.getKey()), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        repository.delete(getById(id));
    }

    private Book getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
    }
}
