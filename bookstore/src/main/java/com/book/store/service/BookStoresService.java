package com.book.store.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.book.store.model.BookDao;
import com.book.store.repository.BookRepository;

@Service
public class BookStoresService {
	
	private Path fileStorageLocation;
	
	@Autowired
	private BookRepository bookRepository;
	
	/*
	 * @Autowired public BookStoresService(BookDao fileStorageProperties) {
	 * this.fileStorageLocation = Paths.get(fileStorageProperties.getPhoto())
	 * .toAbsolutePath().normalize();
	 * 
	 * try {
	 * 
	 * Files.createDirectories(this.fileStorageLocation);
	 * 
	 * } catch (Exception ex) {
	 * 
	 * ex.getLocalizedMessage();
	 * 
	 * }
	 * 
	 * }
	 */

    public void save(BookDao book, MultipartFile file) throws Exception {
    	
    	String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
    	String fileName = "";

    	try {
    		if(originalFileName.contains("..")) {
    			throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);

    		}
    		String fileExtension = "";

    		try {

    			fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

    		} catch(Exception e) {

    			fileExtension = "";

    		}

    		fileName = book.getName() + "_" + book.getCreated() + fileExtension;

    		Path targetLocation = this.fileStorageLocation.resolve(fileName);

    		Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

    		Optional<BookDao> doc = bookRepository.findById(book.getId());

    		if(doc != null) {

    			doc.get().setName(book.getName());

    			doc.get().setAuthor(book.getAuthor());

    			doc.get().setCreated(new Date().toString());

    			doc.get().setIsbn(book.getIsbn());
    			
    			doc.get().setPrice(book.getPrice());
    			
    			doc.get().setPhoto(fileName);

    			bookRepository.save(doc.get());



    		} else {

    			BookDao newDoc = new BookDao(book.getIsbn(), book.getPrice(), book.getCreated(), book.getName(), book.getAuthor());
    			
    			newDoc.setPhoto(fileName);

    			bookRepository.save(newDoc);

    		}


    	} catch (IOException ex) {

    		ex.getLocalizedMessage();

    	}
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public void update(BookDao book) {
        bookRepository.save(book);
    }
    
    public Object findById(Long id) {
        return bookRepository.findById(id);
    }
    
    public List<BookDao> findAll()
    {
    	return bookRepository.findAll();
    }

    
    

}
