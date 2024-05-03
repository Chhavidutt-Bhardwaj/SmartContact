package com.smart.Dao;

import com.smart.model.Contact;
import com.smart.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer>{
	//	pagination...
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactByUser(@Param("userId") long userId, Pageable perPageable);
	
	//search 
	public List<Contact> findByNameContainingAndUser(String name, User user);
}
