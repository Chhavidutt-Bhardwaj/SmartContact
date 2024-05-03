package com.smart.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.Dao.ContactRepo;
import com.smart.Dao.UserRepo;
import com.smart.helper.GoogleHelper;
import com.smart.helper.Message;
import com.smart.model.Contact;
import com.smart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ContactRepo contactRepo;

	@Autowired
	private GoogleHelper googleHelper;


	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("username is " + userName);

		User user = userRepo.getUserByEmail(userName);
		System.out.println("User" + user);
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String dashBoard(Model model, Principal principal) {

		return "user/user_dashboard";
	}

	// open add contact
	@RequestMapping("/add-contact")
	public String openAddContact(Model model) {
		model.addAttribute("title", "Add - contact ");
		model.addAttribute("contact", new Contact());
		return "user/add-contact";
	}

	// processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			
			String name = principal.getName();
			User user = this.userRepo.getUserByEmail(name);

			//processing image
			if(file.isEmpty()) {
				//empty then try msg
				contact.setImage("default.png");
			}else {
				//file to upload
				contact.setImage(file.getOriginalFilename());
				File uploadFile=new ClassPathResource("static/image").getFile();
				Path path=Paths.get(uploadFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image loaded");
			}
			contact.setName(contact.getName().toUpperCase());
			contact.setUser(user);
			//contact.setImage("default.png");
			user.getContact().add(contact);
			// String fileName = this.helper.uploadImage(path, fileUpload);

			this.userRepo.save(user);

			System.out.println("Added sucess");
			System.out.println("Data of contact" + contact);

			// msg success
			session.setAttribute("message", new Message("Your Contact is added !!", "success"));

		} catch (Exception e) {
			e.getStackTrace();
			System.out.println("Error"+e.getMessage());
			session.setAttribute("message", new Message("Sometime is wrong", "danger"));
		}

		return "user/add-contact";
	}

	// view contacts
	// per page contact show with current page
	@GetMapping("/show-contact/{page}")
	public String showContact(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "View Contact");
		String userName = principal.getName();
		User user = this.userRepo.getUserByEmail(userName);

		// show all contacts
		
		Pageable pageable = PageRequest.of(page, 2);

		Page<Contact> contacts = this.contactRepo.findContactByUser(user.getId(), pageable);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPage", contacts.getTotalPages());

		return "user/show-contact";
	}

	// showing particular contact
	@GetMapping("/contact/{cId}")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		Optional<Contact> contactOptional = this.contactRepo.findById(cId);
		Contact contact = contactOptional.get();

		// check the user
		String userName = principal.getName();
		User user = this.userRepo.getUserByEmail(userName);

		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}

		return "user/contact-detail";
	}

	// delete a contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, Principal principal,
			HttpSession session) {
		Optional<Contact> contactOptional = this.contactRepo.findById(cId);
		Contact contact = contactOptional.get();

		// check the user
		String userName = principal.getName();
		User user = this.userRepo.getUserByEmail(userName);

		if (user.getId() == contact.getUser().getId()) {
			// user.getContact().remove(contact);
			contact.setUser(null);

			this.contactRepo.deleteById(cId);

			session.setAttribute("message", new Message("contact deleted successfully", "success"));
		}
		return "redirect:/user/show-contact/0";
	}

	// updating a contact handler
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId") Integer cId, Model model) {
		model.addAttribute("title", "Update Contact");
		Contact contact = this.contactRepo.findById(cId).get();
		model.addAttribute("contact", contact);
		return "/user/contact-update";
	}

	// save the updated contact
	@PostMapping("/process-update")
	public String updateFormContact(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file, Principal principal, Model model,
			HttpSession session) {
		try {
			Contact oldContact = this.contactRepo.findById(contact.getcId()).get();
			//image check
			if(!file.isEmpty()) {
				//rewrite 
				File uploadFile=new ClassPathResource("static/image").getFile();
				Path path=Paths.get(uploadFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			}else {
				contact.setImage(oldContact.getImage());
			}
			System.out.println("contact" + contact.getName());
			User user = this.userRepo.getUserByEmail(principal.getName());
			contact.setUser(user);
			this.contactRepo.save(contact);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/contact/" + contact.getcId();
	}

	// profile page handler
	@GetMapping("/profile")
	public String profile(Model model) {
		model.addAttribute("title", "Profile Page");
		return "user/profile";
	}

	/**
	 * get contact count
	 */
	@GetMapping("/getContactCount/{id}")
	public int contactCount(Principal principal){
		String userNAme = principal.getName();
		return 5;
	}

}
