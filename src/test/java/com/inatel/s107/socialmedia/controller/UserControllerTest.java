package com.inatel.s107.socialmedia.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.inatel.s107.socialmedia.jpa.UserRepository;
import com.inatel.s107.socialmedia.model.Post;
import com.inatel.s107.socialmedia.model.User;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String API_URL = "/api/user";
    private static final String NAME = "John";
    private static final String EMAIL = "john@mail.com";
    private static final String PASSWORD = "123456";
    private static final String POST_1_TITLE = "Post 1";
    private static final String POST_1_CONTENT = "Content 1";
    private static final String POST_2_TITLE = "Post 2";
    private static final String POST_2_CONTENT = "Content 2";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void createUser_shouldReturnUser() throws Exception {
        User user = new User();

        user.setId(UUID.randomUUID());
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"" + NAME + "\", \"email\": \"" + EMAIL + "\", \"password\": \"" + PASSWORD
                        + "\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.password").value(PASSWORD));
    }

    @Test
    public void getUserById_shouldReturnUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();

        user.setId(userId);
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/" + userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.password").value(PASSWORD));
    }

    @Test
    public void getUserById_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/" + userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserPosts_shouldReturnPosts() throws Exception {
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

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/" + userId + "/posts")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(POST_1_TITLE))
                .andExpect(jsonPath("$[0].content").value(POST_1_CONTENT))
                .andExpect(jsonPath("$[1].title").value(POST_2_TITLE))
                .andExpect(jsonPath("$[1].content").value(POST_2_CONTENT));
    }

    @Test
    public void getUserPosts_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL + "/" + userId + "/posts")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
