package com.blog.service;

import com.blog.payloads.CommentDto;

public interface CommentService {
	
	CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId, String name);
	
	void delete(Integer commentId);
	
	

}
