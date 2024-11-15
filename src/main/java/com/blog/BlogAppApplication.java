package com.blog;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blog.entities.Role;
import com.blog.repository.RoleRepo;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner{
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(passwordEncoder.encode("4545"));
		
		try {
			Role role = new Role();
			role.setId(501);
			role.setName("ADMIN_USER");
			
			Role role1 = new Role();
			role1.setId(502);
			role1.setName("NORMAL_USER");
			
			List<Role> roles = Arrays.asList(role,role1);
			
			List<Role> result = roleRepo.saveAll(roles);
			
			result.forEach(r -> System.out.println(r.getName()));
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}

}
