package com.book.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.book.store.model.BookDao;
import com.book.store.service.BookStoresService;

@RestController
@RequestMapping("/book")
public class BookController {
	
	@Autowired
	private BookStoresService bookStoreService;
	
	@GetMapping("/")
    public ResponseEntity<List<BookDao>> getAllBooks() {
        List<BookDao> allBooks = bookStoreService.findAll();
        if (allBooks == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (allBooks.isEmpty())
            return new ResponseEntity<>(allBooks, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object getBook(@PathVariable Long id) {
    	BookDao currentBook = (BookDao)bookStoreService.findById(id);
        if (currentBook != null)
        {
        return new ResponseEntity<>(currentBook, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }

    
    @PostMapping("/save")
    public ResponseEntity<BookDao> saveBook(@RequestParam("file") MultipartFile file,
    		@RequestParam("isbn") String isbn,
    		@RequestParam("price") String price,
    		@RequestParam("created") String created,
    		@RequestParam("name") String name,
    		@RequestParam("author") String author) {
    	
        HttpHeaders headers = new HttpHeaders();
        BookDao book = new BookDao(isbn, price, created, name, author);
        try {
			bookStoreService.save(book, file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new ResponseEntity<>(book, headers, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDao> updateBook(@PathVariable("id") Long id, @RequestBody BookDao book) {
        BookDao currentBook = (BookDao)bookStoreService.findById(id);
        if (currentBook != null) {
            bookStoreService.update(currentBook);
            return new ResponseEntity<>(currentBook, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDao> deleteBook(@PathVariable("id") Long id) {
    	BookDao currentBook = (BookDao)bookStoreService.findById(id);
        if (currentBook != null)
        {
        bookStoreService.delete(id);
        return new ResponseEntity<>(currentBook, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
