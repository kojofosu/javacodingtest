package com.mcdev.javacodingtest;

import com.mcdev.javacodingtest.controller.AuthorController;
import com.mcdev.javacodingtest.controller.CategoryController;
import com.mcdev.javacodingtest.controller.PostController;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavacodingtestApplicationTests {

	@Autowired
	private AuthorController authorController;

	@Autowired
	private PostController postController;

	@Autowired
	private CategoryController categoryController;

	@Test
	void contextLoads() throws Exception{
		/*asserting that the context is creating controllers*/
		assertThat(authorController).isNotNull();
		assertThat(postController).isNotNull();
		assertThat(categoryController).isNotNull();
	}

}
