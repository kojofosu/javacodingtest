package com.mcdev.javacodingtest.controller;

import com.google.gson.Gson;
import com.mcdev.javacodingtest.Type;
import com.mcdev.javacodingtest.config.Config;
import com.mcdev.javacodingtest.exchange.Service;
import com.mcdev.javacodingtest.model.Author;
import com.mcdev.javacodingtest.model.AuthorListResponse;
import com.mcdev.javacodingtest.model.AuthorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthorController.class)
@RunWith(SpringRunner.class)
public class AuthorControllerTest {

    private final Gson gson = new Gson();

    @MockBean
    private Service service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAuthors_200() throws Exception{
        Author author = createAuthor();

        AuthorListResponse response = new AuthorListResponse();
        response.setTimestamp(new Date().toString());
        response.setData(List.of(author));
        response.setStatus(HttpStatus.OK);

        when(service.getAuthorList()).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get(Config.AUTHOR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();

        assertSuccess(resultContent, Type.LIST);
    }

    @Test
    public void getAuthorByEmail_200() throws Exception {
        Author author = createAuthor();

        AuthorResponse response = new AuthorResponse();
        response.setTimestamp(new Date().toString());
        response.setData(author);
        response.setStatus(HttpStatus.OK);

        when(service.getAuthorByEmail("edue@gmail.com")).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get(Config.AUTHOR_BASE_URL + "/edue@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();
        assertSuccess(resultContent, Type.SINGLE);
    }

    @Test
    public void postAuthor_201() throws Exception {
        Author author = createAuthor();

        AuthorResponse response = new AuthorResponse();
        response.setTimestamp(new Date().toString());
        response.setStatus(HttpStatus.CREATED);
        response.setData(author);

        when(service.addAuthor(any(Author.class))).thenReturn(response);

       MvcResult mvcResult = this.mockMvc.perform(
                post(Config.AUTHOR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(author))
                        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();
        assertSuccess(resultContent, Type.SINGLE);
    }

    @Test
    public void updateAuthor_200() throws Exception {
        Author author = createAuthor();

        AuthorResponse response = new AuthorResponse();
        response.setData(author);
        response.setStatus(HttpStatus.OK);
        response.setTimestamp(new Date().toString());

        when(service.updateAuthor(any(Author.class))).thenReturn(response);

        MvcResult mvcResult = this.mockMvc.perform(put(Config.AUTHOR_BASE_URL)
                        .content(gson.toJson(author))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = mvcResult.getResponse().getContentAsString();
        assertSuccess(resultContent, Type.SINGLE);
    }

    @Test
    public void deleteUser_200() throws Exception {
        AuthorResponse response = new AuthorResponse();
        response.setTimestamp(new Date().toString());
        response.setStatus(HttpStatus.OK);

        when(service.deleteAuthor("edue@gmail.com")).thenReturn(response);

        MvcResult mvcResult = this.mockMvc.perform(delete(Config.AUTHOR_BASE_URL + "/edue@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    private Author createAuthor() {
        Author author = new Author();
        author.setEmail("kojo@gmail.com");
        author.setFirstname("kofosu");
        author.setLastname("edue");
        author.setPhone("0209152068");
        return author;
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


}
