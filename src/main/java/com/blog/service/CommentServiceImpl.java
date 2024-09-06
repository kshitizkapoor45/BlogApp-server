package com.blog.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.Comment;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFound;
import com.blog.payloads.CommentDto;
import com.blog.repository.CommentRepo;
import com.blog.repository.PostRepo;
import com.blog.repository.UserRepo;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId, String name) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFound("User", "User_id", userId));
		
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFound("Post", "Post_Id", postId));
		
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		
		comment.setPost(post);
		comment.setUser(user);
		
		Comment savedComment = this.commentRepo.save(comment);
		
		CommentDto responseDto = this.modelMapper.map(savedComment, CommentDto.class);
		responseDto.setName(user.getName());
		
		 return responseDto;
	}

	@Override
	public void delete(Integer commentId) {
		
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFound("Comment", "Comment_id", commentId));
	      
		this.commentRepo.delete(comment);
	}

}
