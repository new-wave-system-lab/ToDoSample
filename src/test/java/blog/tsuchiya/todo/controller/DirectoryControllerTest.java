package blog.tsuchiya.todo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import blog.tsuchiya.todo.CsvDataSetLoader;
import blog.tsuchiya.todo.ToDoSampleApplication;
import blog.tsuchiya.todo.WithMockCustomUser;

@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class, WithSecurityContextTestExecutionListener.class })
@AutoConfigureMockMvc
@SpringBootTest(classes = ToDoSampleApplication.class)
@Transactional
class DirectoryControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("/dir/createにgetでアクセスできる")
	@DatabaseSetup("/testData/")
	@WithMockCustomUser(username = "admin", password = "password")
	void getDirCreateTest() throws Exception {
		this.mockMvc.perform(get("/dir/create")).andExpect(status().isOk()).andExpect(view().name("layout/layout"));
	}

	@Test
	@DisplayName("/dir/createではユーザーが新規登録できる")
	@DatabaseSetup("/testData/")
	@ExpectedDatabase(value = "/testData/dir/create/", assertionMode = DatabaseAssertionMode.NON_STRICT)
	@WithMockCustomUser(username = "admin", password = "password")
	void addUser() throws Exception {
		this.mockMvc.perform(
				post("/dir/create").
				param("name", "ディレクトリ5").
				param("id", "").
				with(SecurityMockMvcRequestPostProcessors.csrf()));
	}

}
