package com.springboot.blog.payload;

import java.util.List;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "PostDto Model Information")
public class PostDto2 {

	private long id;
	
	@Schema(description = "Blog post Title")
	@NotEmpty
	@Size(min=2,message="post title should have at least 2 characters")
	private String title;
	
	@Schema(description = "Blog post description")
	@NotEmpty
	@Size(min=10,message = "description should have atleast 10 characters")
	private String description;
	
	@Schema(description = "Blog post content")
	@NotEmpty
	private String content;
	private Set<CommentDto> comments;
	
	private Long categoryId;
	
	private List<String> tags;
}
