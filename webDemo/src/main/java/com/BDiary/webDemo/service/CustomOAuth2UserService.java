package com.BDiary.webDemo.service;

import com.BDiary.webDemo.dto.CustomOAuth2User;
import com.BDiary.webDemo.dto.GoogleResponse;
import com.BDiary.webDemo.dto.OAuth2Response;
import com.BDiary.webDemo.entity.UserEntity;
import com.BDiary.webDemo.entity.user;
import com.BDiary.webDemo.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    //DefaultOAuth2UserService OAuth2UserService의 구현체

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        user existData = userRepository.findByGooglename(username);

        String role = "ROLE_USER";
        if (existData == null) {

            user userEntity = new user();
            userEntity.setGooglename(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setRole(role);


            userRepository.save(userEntity);
        }
        else {

            existData.setGooglename(username);
            existData.setEmail(oAuth2Response.getEmail());

            role = existData.getRole();

            userRepository.save(existData);
        }

        return new CustomOAuth2User(oAuth2Response, role);
    }
}