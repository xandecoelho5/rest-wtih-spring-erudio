package com.xandecoelho5.restwithspringerudio.controller;

import com.xandecoelho5.restwithspringerudio.data.vo.v1.BookVO;
import com.xandecoelho5.restwithspringerudio.service.BookService;
import com.xandecoelho5.restwithspringerudio.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Book", description = "Endpoints for Managing Books")
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(
            summary = "Find all books", description = "Find all books", tags = {"Book"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookVO.class)))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")
            }
    )
    public List<BookVO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(
            summary = "Finds a book", description = "Finds a book", tags = {"Book"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = BookVO.class))}),
                    @ApiResponse(description = "No Content", responseCode = "204"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")
            }
    )
    public BookVO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(
            summary = "Adds a New Book", description = "Adds a New Book, by passing in a JSON, XML or YML representation", tags = {"Book"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {@Content(schema = @Schema(implementation = BookVO.class))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")
            }
    )
    public BookVO create(@RequestBody BookVO book) {
        return service.create(book);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(
            summary = "Updated a Book", description = "Updates a Book, by passing in a JSON, XML or YML representation", tags = {"Book"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200", content = {@Content(schema = @Schema(implementation = BookVO.class))}),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")
            }
    )
    public BookVO update(@RequestBody BookVO book) {
        return service.update(book);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletes a Book", description = "Deletes a Book, by passing in a JSON, XML or YML representation", tags = {"Book"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not Found", responseCode = "404"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
