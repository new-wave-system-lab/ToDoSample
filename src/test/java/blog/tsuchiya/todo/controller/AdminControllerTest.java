/**
 * Controllerの単体テストサンプル。
 * 結合テストでは @SpringBootTest を使っているが、単体テストでは
 * @WebMvcTest を使っている。<br>
 * @SpringBootTest より @WebMvcTest の方が実行速度が早いため、
 * 必要性がなければ @WebMvcTest を使ったテストを作るべき。<br>
 * なお、このテストサンプル（AdminControllerTest）について言えば、
 * @SpringBootTest では11秒、 @WebMvcTest では8秒くらいかかった。
 */
package blog.tsuchiya.todo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import blog.tsuchiya.todo.Role;
import blog.tsuchiya.todo.model.Directory;
import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.service.DirectoryService;
import blog.tsuchiya.todo.service.ToDoUserService;
import blog.tsuchiya.todo.service.UserDetailsServiceImpl;

// テスト対象のControllerを指定する
@WebMvcTest(controllers = AdminController.class)
// MockMvcの設定を行う
@AutoConfigureMockMvc
public class AdminControllerTest {
	// @AutoConfigureMockMvc アノテーションがあるので、 @Autowiredできる
	@Autowired
	private MockMvc mockMvc;

	// Intercepterとテスト対象のController、ログイン処理でDIするインスタンスはすべてモックを作っておく
	/** DirectoryIntercepterで使うモック */
	@MockBean
	private DirectoryService ds;

	/** DirectoryIntercepterとAdminControllerで使うモック */
	@MockBean
	private ToDoUserService tdus;

	/** ユーザ認証（Spring Security）で使うモック */
	@MockBean
	private UserDetailsServiceImpl udsi;

	@Test
	@DisplayName("管理者ユーザでユーザ登録画面が表示できる")
	// ログインしているユーザを擬似的に追加。DWEではこの指定方法ではなく、
	// blog.tsuchiya.todo.WithMockCustomUserを使うべきかもしれない。
	@WithMockUser(username = "admin", roles = "ADMIN")
	void getCreate() throws Exception {
		// 各モックのメソッドを初期化、ここではDirectoryIntercepterで使われるメソッドの定義をしている
		final String name = "admin";
		final ToDoUser user = new ToDoUser();
		user.setId(1L);
		user.setUsername(name);
		Mockito.when(tdus.getToDoUserByName(name)).thenReturn(user);
		Mockito.when(ds.getDirectoriesByUserId(user.getId())).thenReturn(new ArrayList<Directory>());
		// 自動フォーマット機能を指定範囲だけ無効にするアノテーション
		// @formatter:off
		// /admin/createにアクセスした場合のテスト実行、実際にはもっとたくさんの項目をテストすることになるはず
		this.mockMvc.
			perform(get("/admin/create")).
			andExpect(status().isOk()).
			andExpect(view().name("layout/layout")).
			andExpect(
				model().
					attribute(
						"roles",
						hasItem(Role.ADMIN)
					));
		 
		// @formatter:on

	}
}
