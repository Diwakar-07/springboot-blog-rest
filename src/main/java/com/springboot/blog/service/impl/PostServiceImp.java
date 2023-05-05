package com.springboot.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;

@Service
public class PostServiceImp implements PostService{

	private PostRepository postRepository;
	
	private ModelMapper mapper;
	
	private CategoryRepository categoryRepository;
	
	
	

	public PostServiceImp(PostRepository postRepository, ModelMapper mapper, CategoryRepository categoryRepository) {
		super();
		this.postRepository = postRepository;
		this.mapper = mapper;
		this.categoryRepository = categoryRepository;
	}

	//************************************************************************************************
	
	

	@Override
	public PostDto createPost(PostDto postDto) {
		// TODO Auto-generated method stub
		
		Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("category", "id", postDto.getCategoryId()));
		
		//convert DTO into Entity
		Post post = mapToEntity(postDto);
		
		post.setCategory(category);
		
//		Post post = new Post();
//		post.setTitle(postDto.getTitle());
//		post.setDescription(postDto.getDescription());
//		post.setContent(postDto.getContent());
		
		Post newPost=postRepository.save(post);
		
		//convert Entity into DTO
		PostDto postRespone = mapToDto(newPost);
		
//		PostDto postRespone = new PostDto();
//		postRespone.setId(newPost.getId());
//		postRespone.setTitle(newPost.getTitle());
//		postRespone.setDescription(newPost.getDescription());
//		postRespone.setContent(newPost.getContent());
		
		
		
		return postRespone;
	}

	//*******************************************************************************************
	
	
	
	
//	//converted Entity into DTO
//	private PostDto mapToDto(Post post) {
//		
//		
//		PostDto postDto = new PostDto();
//		postDto.setId(post.getId());
//		postDto.setTitle(post.getTitle());
//		postDto.setDescription(post.getDescription());
//		postDto.setContent(post.getContent());
//		
//		return postDto;
//	}
//	
//	
//	//converted DTO to Entity
//	private Post mapToEntity(PostDto postDto) {
//		Post post = new Post();
//		post.setTitle(postDto.getTitle());
//		post.setDescription(postDto.getDescription());
//		post.setContent(postDto.getContent());
//		return post;
//	}

	
	//*****************************************************************************************************
	
	
	//converting Entity to DTO
	private PostDto mapToDto(Post post) {
		
		PostDto postDto = mapper.map(post, PostDto.class);
		return postDto;
		
	}
	
	//converting DTO into Entity
	private Post mapToEntity(PostDto postDto) {
		
		Post post = mapper.map(postDto, Post.class);
		return post;
	}
	
	
	@Override
	public PostResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir) {
		
		Sort sort =sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		//create Pageable instance
		
//		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page<Post> posts = postRepository.findAll(pageable);
		
		//get content form page object
		List<Post> listOfPosts =posts.getContent();
		
		List<PostDto> content= listOfPosts.stream().map(post ->mapToDto(post)).collect(Collectors.toList());
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(posts.getNumber());
		postResponse.setPageSize(posts.getSize());
		postResponse.setTotalElements(posts.getTotalElements());
		postResponse.setTotalPages(posts.getTotalPages());
		postResponse.setLast(posts.isLast());
		
		return postResponse;
	}


	@Override
	public PostDto getPostById(long id) {
		Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
		return mapToDto(post);
	}


	@Override
	public PostDto updatePost(PostDto postDto, long id) {
		
		//get post by id from the database
		Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
		
		Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("category", "id", postDto.getCategoryId()));
		
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		post.setCategory(category);
		
		Post updatedPost = postRepository.save(post);
		return mapToDto(updatedPost);
	}


	@Override
	public void deletePostById(long id) {
		// get post by id from database
		Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
		postRepository.delete(post);
		
	}

	@Override
	public List<PostDto> findByCategoryId(Long categoryId) {
		
		categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category", "id", categoryId));
		List<Post> posts = postRepository.findByCategoryId(categoryId);
		
		return posts.stream().map((post)-> mapToDto(post)).collect(Collectors.toList());
	}

}
