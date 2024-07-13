package com.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {

	private int id;
	
	@NotBlank
	@Size(min = 3, message = "Username must be atleast 3 Characters")
	private String name;
	
	@Email(message = "Email Address is not valid")
	private String email;
	
	@NotBlank
	@Size(min = 5, max = 12, message = "Password must between 5 to 12 characters")
	private String password;
	
	@NotBlank(message = "Please Enter your About")
	private String about;
	
	private Set<CommentDto> comments = new HashSet<>();
}
