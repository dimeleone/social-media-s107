package com.inatel.s107.socialmedia.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inatel.s107.socialmedia.jpa.PostRepository;
import com.inatel.s107.socialmedia.jpa.UserRepository;
import com.inatel.s107.socialmedia.model.Comment;
import com.inatel.s107.socialmedia.model.Post;
import com.inatel.s107.socialmedia.model.User;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    private static final String API_URL = "/api/post";
    private static final String NAME = "John";
    private static final String EMAIL = "john@mail.com";
    private static final String PASSWORD = "123456";
    private static final String POST_1_TITLE = "Post 1";
    private static final String POST_1_CONTENT = "Content 1";
    private static final String POST_2_TITLE = "Post 2";
    private static final String POST_2_CONTENT = "Content 2";
    private static final String COMMENT_CONTENT = "Comment 1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @Test
    public void createPost_shouldReturnPost() throws Exception {
        User user = new User();

        user.setId(UUID.randomUUID());
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);

        Post post = new Post();

        post.setId(UUID.randomUUID());
        post.setTitle(POST_1_TITLE);
        post.setContent(POST_1_CONTENT);
        post.setUser(user);

        Mockito.when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(post);

        mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"title\": \"" + POST_1_TITLE + "\", \"content\": \"" + POST_1_CONTENT + "\", \"user\": { \"id\": \"" + user.getId() + "\" } }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(POST_1_TITLE))
                .andExpect(jsonPath("$.content").value(POST_1_CONTENT))
                .andExpect(jsonPath("$.user.id").value(user.getId().toString()));
    }

    @Test
    public void createPost_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"title\": \"" + POST_1_TITLE + "\", \"content\": \"" + POST_1_CONTENT + "\", \"user\": { \"id\": \"" + userId + "\" } }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPostById_shouldReturnPost() throws Exception {
        UUID postId = UUID.randomUUID();
        User user = new User();

        user.setId(UUID.randomUUID());
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);

        Post post = new Post();

        post.setId(postId);
        post.setTitle(POST_1_TITLE);
        post.setContent(POST_1_CONTENT);
        post.setUser(user);

        Mockito.when(postRepository.findById(any(UUID.class))).thenReturn(Optional.of(post));

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/" + postId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(POST_1_TITLE))
                .andExpect(jsonPath("$.content").value(POST_1_CONTENT))
                .andExpect(jsonPath("$.user.id").value(user.getId().toString()));
    }

    @Test
    public void getPostById_whenPostDoesNotExist_shouldReturnNotFound() throws Exception {
        UUID postId = UUID.randomUUID();

        Mockito.when(postRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/" + postId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPostComments_shouldReturnComments() throws Exception {
        UUID postId = UUID.randomUUID();

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);

        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setContent(COMMENT_CONTENT);

        Post post = new Post();

        post.setId(postId);
        post.setTitle(POST_1_TITLE);
        post.setContent(POST_1_CONTENT);
        post.setUser(user);
        post.getComments().add(comment);

        Mockito.when(postRepository.findById(any(UUID.class))).thenReturn(Optional.of(post));

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/" + postId + "/comment")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(COMMENT_CONTENT));
    }

    @Test
    public void getPostComments_whenPostDoesNotExist_shouldReturnNotFound() throws Exception {
        UUID postId = UUID.randomUUID();

        Mockito.when(postRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/" + postId + "/comment")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllPosts_shouldReturnPosts() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();

        Post post1 = new Post();
        post1.setId(UUID.randomUUID());
        post1.setTitle(POST_1_TITLE);
        post1.setContent(POST_1_CONTENT);

        Post post2 = new Post();
        post2.setId(UUID.randomUUID());
        post2.setTitle(POST_2_TITLE);
        post2.setContent(POST_2_CONTENT);

        user.setId(userId);
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.getPosts().add(post1);
        user.getPosts().add(post2);

        Mockito.when(postRepository.findAll()).thenReturn(user.getPosts());

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(POST_1_TITLE))
                .andExpect(jsonPath("$[0].content").value(POST_1_CONTENT))
                .andExpect(jsonPath("$[1].title").value(POST_2_TITLE))
                .andExpect(jsonPath("$[1].content").value(POST_2_CONTENT));
    }
}
