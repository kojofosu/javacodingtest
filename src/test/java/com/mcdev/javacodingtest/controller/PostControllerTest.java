package com.mcdev.javacodingtest.controller;

import com.google.gson.Gson;
import com.mcdev.javacodingtest.Type;
import com.mcdev.javacodingtest.config.Config;
import com.mcdev.javacodingtest.exchange.Service;
import com.mcdev.javacodingtest.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@RunWith(SpringRunner.class)
public class PostControllerTest {
    private final Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Service service;

    @Test
    public void addPost_201() throws Exception {
        Post post = createPost();

        PostResponse response = new PostResponse();
        response.setStatus(HttpStatus.CREATED);
        response.setData(post);
        response.setTimestamp(new Date().toString());

        when(service.addPost(any(Post.class))).thenReturn(response);

        MvcResult mvcResult = this.mockMvc.perform(post(Config.POST_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(post)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();
        assertSuccess(resultContent, Type.SINGLE);
    }

    @Test
    public void getPostByTitle_200() throws Exception {
        PostResponse response = new PostResponse();
        response.setStatus(HttpStatus.OK);
        response.setData(createPost());
        response.setTimestamp(new Date().toString());

        when(service.getPostByTitle("Accra")).thenReturn(response);

        MvcResult mvcResult = this.mockMvc.perform(get(Config.POST_BASE_URL+"/Accra")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();
        assertSuccess(resultContent, Type.SINGLE);
    }

    @Test
    public void getPosts_200() throws Exception {
        PostListResponse response = new PostListResponse();
        response.setStatus(HttpStatus.OK);
        response.setData(List.of(createPost()));
        response.setTimestamp(new Date().toString());

        when(service.getPostList()).thenReturn(response);

        MvcResult mvcResult = this.mockMvc.perform(get(Config.POST_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();
        assertSuccess(resultContent, Type.LIST);
    }

    @Test
    public void updatePost_200() throws Exception{
        Post post = createPost();

        PostResponse response = new PostResponse();
        response.setStatus(HttpStatus.OK);
        response.setTimestamp(new Date().toString());
        response.setData(post);

        when(service.updatePost(any(Post.class))).thenReturn(response);

        MvcResult mvcResult = this.mockMvc.perform(put(Config.POST_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(post)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();
        assertSuccess(resultContent, Type.SINGLE);
    }

    @Test
    public void deletePost_200() throws Exception {
        PostResponse response = new PostResponse();
        response.setTimestamp(new Date().toString());
        response.setStatus(HttpStatus.OK);

        when(service.deletePost("Accra")).thenReturn(response);

        MvcResult mvcResult = this.mockMvc.perform(delete(Config.POST_BASE_URL + "/Accra")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    private void assertSuccess(String resultContent, Type type) {
        if (type.equals(Type.LIST)) {
            AuthorListResponse response = gson.fromJson(resultContent, AuthorListResponse.class);
            assertNull("FAIL : Error message is expected to be null", response.getErrormessage());
            assertNotNull("FAIL : Timestamp is expected to be non-null", response.getTimestamp());
            assertNotNull("FAIL : Response data is expected to be non-null", response.getData());
        } else {
            AuthorResponse response = gson.fromJson(resultContent, AuthorResponse.class);
            assertNull("FAIL : Error message is expected to be null", response.getErrormessage());
            assertNotNull("FAIL : Timestamp is expected to be non-null", response.getTimestamp());
            assertNotNull("FAIL : Response data is expected to be non-null", response.getData());
        }
    }

    private Post createPost() {
        Post post = new Post();
        post.setTitle("Accra");
        post.setAuthor(createAuthor());
        post.setText("Accra is dangerous.");
        post.setCategories(List.of(createCategory()));
        return post;
    }

    private Author createAuthor() {
        Author author = new Author();
        author.setEmail("kojo@gmail.com");
        author.setFirstname("kofosu");
        author.setLastname("edue");
        author.setPhone("0209152068");
        return author;
    }

    private Category createCategory() {
        Category category = new Category();
        category.setName("LIVE");
        category.setDescription("A live something");
                return category;
    }
}
