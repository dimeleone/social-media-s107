package com.inatel.s107.socialmedia.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inatel.s107.socialmedia.jpa.CommentRepository;
import com.inatel.s107.socialmedia.jpa.PostRepository;
import com.inatel.s107.socialmedia.jpa.UserRepository;
import com.inatel.s107.socialmedia.model.Comment;
import com.inatel.s107.socialmedia.model.Post;
import com.inatel.s107.socialmedia.model.User;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    private static final String API_URL = "/api/post";
    private static final String NAME = "John";
    private static final String EMAIL = "john@mail.com";
    private static final String PASSWORD = "123456";
    private static final String POST_1_TITLE = "Post 1";
    private static final String POST_1_CONTENT = "Content 1";
    private static final String COMMENT_CONTENT = "Comment 1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Test
    public void createComment_shouldReturnComment() throws Exception {
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

        Comment comment = new Comment();

        comment.setId(UUID.randomUUID());
        comment.setContent(COMMENT_CONTENT);
        comment.setPost(post);

        Mockito.when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        Mockito.when(postRepository.findById(any(UUID.class))).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders.post(API_URL + "/" + post.getId() + "/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"content\": \"" + COMMENT_CONTENT + "\", \"userId\": \"" + user.getId() + "\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(COMMENT_CONTENT))
                .andExpect(jsonPath("$.post.title").value(POST_1_TITLE));
    }

    @Test
    public void createComment_whenPostDoesNotExist_shouldReturnNotFound() throws Exception {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Mockito.when(postRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post(API_URL + "/" + postId + "/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"content\": \"" + COMMENT_CONTENT + "\", \"userId\": \"" + userId + "\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
