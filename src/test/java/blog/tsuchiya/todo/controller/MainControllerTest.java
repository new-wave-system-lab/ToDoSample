package blog.tsuchiya.todo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import blog.tsuchiya.todo.CsvDataSetLoader;
import blog.tsuchiya.todo.ToDoSampleApplication;
import blog.tsuchiya.todo.WithMockCustomUser;

@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
      DependencyInjectionTestExecutionListener.class,
      TransactionalTestExecutionListener.class,
      DbUnitTestExecutionListener.class,
      WithSecurityContextTestExecutionListener.class
    })
@AutoConfigureMockMvc
@SpringBootTest(classes = ToDoSampleApplication.class)
@Transactional
class MainControllerTest {
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	@DisplayName("/では200が返ってくる")
	void rootReturnStatus200() throws Exception{
		// ヘッダの値を指定できる。ここではhostを指定
		this.mockMvc.perform(get("/").header("host", "test.localhost:80"))
			.andExpect(status().isOk())
			.andExpect(view().name("layout/layout"));
	}
	
	@Test
	@DisplayName("adminでログインできる")
	@DatabaseSetup("/testData/")
	void loginOnAdmin() throws Exception {
		// どうやらuserメソッドで与えたユーザー名はusernameとしてログインフォームに与えられる
		// したがって、この手法でログインの確認をする場合はログインページで
		// ユーザ名の属性名をusernameにしないとだめ
		this.mockMvc.perform(formLogin("/").user("admin").password("password"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/todo"));
	}


	@Test
	@DisplayName("/todoではログインしたユーザーのディレクトリ一覧が取得できる")
	@DatabaseSetup("/testData/")
	@WithMockCustomUser(username="admin", password="password")
	void todoHasDirectories() throws Exception{
		//サンプルデータではadminの持つディレクトリの数は３
		this.mockMvc.perform(get("/todo"))
			.andExpect(model().attribute("directories",hasSize(3)))
			// ディレクトリ３という名前のものを持っている
			.andExpect(model().attribute("directories", 
					hasItem(
							hasProperty("name", is("ディレクトリ3"))
					)
			));
	}
}
