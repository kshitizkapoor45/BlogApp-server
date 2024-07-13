package com.blog.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFound;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.repository.CategoryRepo;
import com.blog.repository.PostRepo;
import com.blog.repository.UserRepo;

@Service
public class PostServiceImpl implements PostService{
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFound("User", "User_id", userId));
		
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFound("Category", "Category_Id", categoryId));
		
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost = this.postRepo.save(post);
		
		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
	       
			Post post = this.postRepo.findById(postId)
					.orElseThrow(() -> new ResourceNotFound("Post", "Id", postId));
			
			post.setTitle(postDto.getTitle());
			post.setContent(postDto.getContent());
			post.setImageName(postDto.getImageName());
			
			Post updatedPost = postRepo.save(post);
			return this.modelMapper.map(updatedPost, PostDto.class);
		
	}

	@Override
	public void deletePost(Integer postId) {
	        
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFound("Post", "Id", postId));
				
				this.postRepo.delete(post);
		
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy) {
		
		Pageable p = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
		Page<Post> pagePost = postRepo.findAll(p);
		
		List<Post> posts = pagePost.getContent();
		List<PostDto> postsDto = posts.stream()
				.map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		
		PostResponse poseResponse = new PostResponse();
		poseResponse.setContent(postsDto);
		poseResponse.setPageNumber(pagePost.getNumber());
		poseResponse.setPageSize(pagePost.getSize());
		poseResponse.setTotalElements(pagePost.getTotalElements());
		poseResponse.setTotalPages(pagePost.getTotalPages());
		poseResponse.setLastPage(pagePost.isLast());
		
		return poseResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		  Post post =  this.postRepo.findById(postId)
				  .orElseThrow(() -> new ResourceNotFound("Post", "Post_Id", postId));
		  
		  PostDto postDto = this.modelMapper.map(post, PostDto.class);
		  
		return postDto;
	}

	@Override
	public PostResponse getPostsByCategory(Integer categoryId,Integer pageNumber, Integer pageSize) {
		
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFound("Category", "category_Id", categoryId));
		
		 Pageable pageable = PageRequest.of(pageNumber, pageSize);
	        Page<Post> pagePost = this.postRepo.findByCategory(cat, pageable);

	        List<Post> posts = pagePost.getContent();
	        List<PostDto> postsDto = posts.stream()
	                .map(post -> this.modelMapper.map(post, PostDto.class))
	                .collect(Collectors.toList());

	        PostResponse postResponse = new PostResponse();
	        postResponse.setContent(postsDto);
	        postResponse.setPageNumber(pagePost.getNumber());
	        postResponse.setPageSize(pagePost.getSize());
	        postResponse.setTotalElements(pagePost.getTotalElements());
	        postResponse.setTotalPages(pagePost.getTotalPages());
	        postResponse.setLastPage(pagePost.isLast());

	        return postResponse;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFound("User", "User_Id", userId));
		
		List<Post> posts = this.postRepo.findByUser(user);
		List<PostDto> postsDto = posts.stream()
				.map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		
		return postsDto;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		
		List<Post> posts = this.postRepo.findByTitleContaining(keyword);
		List<PostDto> postsDto = posts.stream()
				.map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		
		return postsDto;
	}


}
