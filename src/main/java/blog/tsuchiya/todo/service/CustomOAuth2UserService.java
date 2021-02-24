/**
 * 未実装のOAuth2UserService。利用するソーシャルログインの種類によってはこれの実装も必要になる。
 */
package blog.tsuchiya.todo.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import blog.tsuchiya.todo.repository.ToDoUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{

	private final ToDoUserRepository tdur;
	
	@Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException{
		OAuth2User user = super.loadUser(oAuth2UserRequest);
		return user;
	}
}
