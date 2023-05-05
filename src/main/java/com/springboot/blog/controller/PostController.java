package com.springboot.blog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostDto2;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "REST APIs for Post Resource")
public class PostController {

	private PostService postService;

	public PostController(PostService postService) {
		super();
		this.postService = postService;
	}
	
	//create blog post
	@Operation(
			summary = "Create Post REST API",
			description = "This API is used to save post into database"
	)
	@ApiResponse(
			responseCode = "201",
			description = "Http Status 201 CREATED"
	)
	@SecurityRequirement(name = "Bear Authentication")
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
		return new ResponseEntity<>(postService.createPost(postDto),HttpStatus.CREATED);
	}
	
	//get all posts rest api
	@Operation(
			summary = "get All Posts REST API",
			description = "This API is used to get all posts from database"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 SUCCESS"
	)
	@GetMapping
	public PostResponse getAllPosts(
			@RequestParam(value="pageNo",defaultValue=AppConstants.DEFAULT_PAGE_NUMBER,required=false)int pageNo,
			@RequestParam(value="pageSize",defaultValue=AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
			@RequestParam(value="sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAILT_SORT_DIRECTION,required = false)String sortDir
			){
		return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
	}
	
	//get post by id
	@Operation(
			summary = "get Post By Id REST API",
			description = "This API is used to get post from database"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 SUCCESS"
	)
	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable(name="id") long id){
		
		return ResponseEntity.ok(postService.getPostById(id));
	}
	
	//update post by id
	@Operation(
			summary = "update Post REST API",
			description = "This API is used to update post in a database"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 SUCCESS"
	)
	@SecurityRequirement(name = "Bear Authentication")
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable long id){
		
		//return new ResponseEntity<>(postService.updatePost(postDto, id),HttpStatus.OK);
	
		PostDto postResponse = postService.updatePost(postDto, id);
		return new ResponseEntity<>(postResponse,HttpStatus.OK);
	
	}
	
	//delete post
	@Operation(
			summary = "Delete Post REST API",
			description = "This API is used to delete post from the database"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 SUCCESS"
	)
	@SecurityRequirement(name = "Bear Authentication")
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable(name="id") long id){
		postService.deletePostById(id);
		return new ResponseEntity<>("post entity deleted successfully", HttpStatus.OK);
	}
	
	//get post by category id
	@GetMapping("/category/{id}")
	public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable("id") Long categoryId){
		
		List<PostDto> postDto = postService.findByCategoryId(categoryId);
		
		return ResponseEntity.ok(postDto);
	}
	
	
	//uri version
	
	@GetMapping("/v1/{id}")
	public ResponseEntity<PostDto2> getPostByIdv2(@PathVariable(name="id") long id){
		
		PostDto postDto = postService.getPostById(id);
		PostDto2 postDto2 = new PostDto2();
		postDto2.setId(postDto.getId());
		postDto2.setTitle(postDto.getTitle());
		postDto2.setDescription(postDto.getDescription());
		postDto2.setContent(postDto.getContent());
		List<String> tags = new ArrayList<>();
		tags.add("java");
		tags.add("Spring Boot");
		postDto2.setTags(tags);
		return ResponseEntity.ok(postDto2);
	}
	
	
	//versioning through query params
	// just change GetMapping("{id}", params="version=1")
	
	
	
	//version through header 
	//GetMapping("{id}", headers="X-API-VERSION=1")
	//key value 
	
	
	//version through content negotiation
	//GetMapping("{id}", produces = "application/sanjeevani,v1+json")
	//through accept and value
}
