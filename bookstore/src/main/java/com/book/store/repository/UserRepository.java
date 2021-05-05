package com.book.store.repository;

import org.springframework.data.repository.CrudRepository;

import com.book.store.model.UserDao;

public interface UserRepository extends CrudRepository<UserDao, Integer> {
	
	UserDao findByUsername(String username);

}
