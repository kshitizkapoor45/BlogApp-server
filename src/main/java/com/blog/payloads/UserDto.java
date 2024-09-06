package com.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {

	private int id;
	
	@NotEmpty
	@Size(min = 3, message = "Username must be atleast 3 Characters")
	private String name;
	
	@Email(message = "Email Address is not valid")
	@NotEmpty
	private String email;
	
	@NotBlank
	@NotEmpty
	@Size(min = 5, max = 12, message = "Password must between 5 to 12 characters")
	private String password;
	
	@NotBlank(message = "Please Enter your About")
	@NotEmpty
	private String about;
	
	private Set<CommentDto> comments = new HashSet<>();
	
	@JsonIgnore
	public String getPassword()
	{
		return password;
	}
	
	@JsonProperty
	public void setPassword(String password)
	{
		this.password = password;
	}
}
