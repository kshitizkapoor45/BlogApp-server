package com.blog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.entities.Role;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFound;
import com.blog.payloads.UserDto;
import com.blog.repository.RoleRepo;
import com.blog.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.dtoTouser(userDto);
		User savedUser = this.userRepo.save(user);
		
		return this.userToDto(savedUser);
		 
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFound("User", "id", userId));
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		
		User updatedUser = this.userRepo.save(user);
		UserDto userDto1 = this.userToDto(updatedUser);
		
		return userDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFound("User", "Id", userId));
		
		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		
		List<User> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream()
				.map(user -> this.userToDto(user)).collect(Collectors.toList());
		
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		
		User user = this.userRepo.findById(userId)
		.orElseThrow(() -> new ResourceNotFound("User", "Id", userId));
		
		this.userRepo.delete(user);
		
	}
	
	public User dtoTouser(UserDto userDto)
	{
		User user = this.modelMapper.map(userDto, User.class);	
		return user;
	}
	
	public UserDto userToDto(User user)
	{
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
	}

	@Override
	public UserDto registerUser(UserDto userDto) {
	  User user = this.modelMapper.map(userDto, User.class);
	  
	  user.setPassword(passwordEncoder.encode(user.getPassword()));
	  
	  //roles
	 Role role = roleRepo.findById(502).get();
	 user.getRoles().add(role);
	 
	 User newuser = userRepo.save(user);
	  
		return this.modelMapper.map(newuser, UserDto.class);
	}
	

}
