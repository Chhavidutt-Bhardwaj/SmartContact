package com.smart.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.Dao.UserRepo;
import com.smart.helper.GoogleHelper;
import com.smart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.util.Arrays;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private GoogleHelper googleHelper;
	
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Smart Contact");
		
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About Smart Contact");
		
		return "about";
	}
	
	@GetMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("title","SignUp - Smart Contact");
		model.addAttribute("user", new User());
		return "signup";
	}
	//handler for custom login
	@GetMapping("/signIn")
	public String customLogin(Model model) {
		model.addAttribute("title","SIgnIn - Smart Contact");
		return "login";
	}
	
	//Handler for registration form
	@RequestMapping(value="/do_register", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,HttpSession session) {
		if(result.hasErrors()) {
			System.out.println("Error"+result.toString());
			model.addAttribute("user", user);
			return "signIn";
		}
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImage("default.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		User resultUser =this.userRepo.save(user);
		
		System.out.println("User" + resultUser);
		return "signup";
	}

	/**
	 * google login
	 */
	@RequestMapping("/getAuthUrl")
	public String getAuthUrl() {
		return googleHelper.redirectUri();
	}

	@RequestMapping("/oauthcallback/code")
	public String oauthcallback(@DefaultValue("error") @QueryParam("error") String error,
								@QueryParam("code") String code, @QueryParam("state") String state, Model model) throws IOException {
		// to get access token using authentication code
		ResponseEntity<String> response = null;
		ResponseEntity<String> response1 = null;
		RestTemplate restTemplate = new RestTemplate();
		String clientId = "1020058965393-3nfgaabpc4ptmtinphhqkae7lnju705i.apps.googleusercontent.com";
		String clientSecret = "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";
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<String>(headers);
		String access_token_url = "https://oauth2.googleapis.com/token";
		access_token_url += "?code=" + code;
		access_token_url += "&client_id=" + clientId;
		access_token_url += "&client_secret=" + clientSecret;
		access_token_url += "&grant_type=authorization_code";
		access_token_url += "&redirect_uri=http://localhost:9595/oauthcallback/code";
		response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
		System.out.println("Access Token Response ---------" + response.getBody());
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(response.getBody());
		String token = node.path("access_token").asText();
		System.out.println(token);
		JsonNode node2 = mapper.readTree(response.getBody());
		String refreshToken = node2.path("refresh_token").asText();
		System.out.println(refreshToken);
		// to get user info using access token
		String url = "https://www.googleapis.com/oauth2/v1/userinfo";
		HttpHeaders headers1 = new HttpHeaders();
		headers1.add("Authorization", "Bearer " + token);
		HttpEntity<String> request1 = new HttpEntity<String>(headers1);
		response1 = restTemplate.exchange(url, HttpMethod.GET, request1, String.class);
		System.out.println("User Detail Response ---------" + response1.getBody());
		ObjectMapper mapper1 = new ObjectMapper();
		JsonNode node1 = mapper1.readTree(response1.getBody());
		String name = node1.path("name").asText();
		System.out.println(name);
//		GoogleUser user = new GoogleUser();
//		user.setName(name);
//		user.setAccessToken(token);
//		user.setRefreshToken(refreshToken);
//		this.googleRepo.save(user);
		// this.googleRepo.save(name,token, refreshToken);
		return "index";

	}
	
}
