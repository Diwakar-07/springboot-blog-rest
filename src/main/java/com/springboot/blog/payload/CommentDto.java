package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {

	private long id;
	
	@NotEmpty(message = "should not be empty")
	private String name;
	
	@NotEmpty(message = "should not be empty")
	@Email
	private String email;
	
	@NotEmpty
	@Size(min = 10, message = "content should contain min 10 characters")
	private String body;
}
