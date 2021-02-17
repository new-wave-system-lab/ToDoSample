/**
 * Mockitoを使ったテストのサンプル
 * Spring Bootに依存しないでテストをできるため、処理が早い。
 */
package blog.tsuchiya.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import blog.tsuchiya.todo.model.Directory;
import blog.tsuchiya.todo.repository.DirectoryRepository;

public class DirectoryServiceTest {

	// テスト対象のクラスでDIされるクラスは@Mockで定義してモック化しておく
	@Mock
	private DirectoryRepository repository;
	
	// @Mockでモック化したインスタンスをDIする
	@InjectMocks
	private DirectoryService service;
	
	// @InjectMocksと@Mockで宣言した内容を実行する。
	// 初期化しないと宣言した内容が実行されず、インスタンスがnullのままになる
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	// 実際のテスト
	@Test
	void mockSampleTest() throws Exception {
		var d = new Directory();
		d.setId(1L);
		d.setName("name");
		// メソッドのモック化
		// whenメソッドでモック化するメソッドを宣言する。今回の場合は任意のLongが渡された場合このモックが実行される。
		// thenReturnで戻り値を定義する。
		Mockito.when(repository.findById(anyLong())).thenReturn(Optional.of(d));
		
		var result = service.findDirectoryById(1L);
		assertThat(result.getName()).isEqualTo(d.getName());
		
		// モック化したメソッドが、引数のとおりに呼び出されたかどうか確認している。
		verify(repository).findById(1L);
	}
}
