package com.smart.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "userData")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Size(min = 2, max = 20, message = "Please filled correct order of Name")
	private String name;

	@Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
			+ "A-Z]{2,7}$", message = "Enter a valid email")
	private String email;

	// @Pattern(regexp = "(0|91)?[6-9][0-9]{9}\\", message = "Please Enter a valid
	// Number")
	private String Number;
	@NotBlank(message = "Password is required")
	private String password;

	private String role;
	private boolean enabled;
	private String image;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Contact> contact = new ArrayList<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<Contact> getContact() {
		return contact;
	}

	public void setContact(List<Contact> contact) {
		this.contact = contact;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(long id, String name, String email, String number, String password, String role, boolean enabled,
			String image, List<Contact> contact) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		Number = number;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.image = image;
		this.contact = contact;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", Number=" + Number + ", password="
				+ password + ", role=" + role + ", enabled=" + enabled + ", image=" + image + ", contact=" + contact
				+ "]";
	}

	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy = GenerationType.AUTO) private int Id; private
	 * String Name; private String Email;
	 * 
	 * @Column(unique = true) private int Number; private String Password; private
	 * String Role; private boolean Enabled; private String ImageUrl;
	 * 
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) private
	 * List<Contact> contact = new ArrayList<>();
	 * 
	 * public int getId() { return Id; } public void setId(int id) { this.Id = id; }
	 * public String getName() { return Name; } public void setName(String name) {
	 * Name = name; } public String getEmail() { return Email; } public void
	 * setEmail(String email) { Email = email; } public int getNumber() { return
	 * Number; } public void setNumber(int number) { Number = number; } public
	 * String getPassword() { return Password; } public void setPassword(String
	 * password) { Password = password; } public String getRole() { return Role; }
	 * public void setRole(String role) { this.Role = role; } public boolean
	 * isEnabled() { return Enabled; } public void setEnabled(boolean enabled) {
	 * this.Enabled = enabled; } public String getImageUrl() { return ImageUrl; }
	 * public void setImageUrl(String imageUrl) { this.ImageUrl = imageUrl; }
	 * 
	 * public List<Contact> getContact() { return contact; } public void
	 * setContact(List<Contact> contact) { this.contact = contact; } public User() {
	 * super();
	 * 
	 * }
	 * 
	 * public User(int id, String name, String email, int number, String password,
	 * String role, boolean enabled, String imageUrl) { super(); Id = id; Name =
	 * name; Email = email; Number = number; Password = password; Role = role;
	 * Enabled = enabled; ImageUrl = imageUrl; }
	 */

}
