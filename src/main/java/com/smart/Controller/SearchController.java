package com.smart.Controller;

import com.smart.Dao.ContactRepo;
import com.smart.Dao.UserRepo;
import com.smart.model.Contact;
import com.smart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactRepo contactRepo;
	//search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query ,Principal principal){
		query= query.toUpperCase();
		User user= this.userRepo.getUserByEmail(principal.getName());
		List<Contact> contacts= this.contactRepo.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}
}
