package com.example.woomansi.data.model;

import java.net.URI;

/**
 * 사용자 계정 정보 모델 클래스
 */
public class UserModel {

    private String idToken;         //  Firebase Uid(고유 토큰정보)
    private String emailId;         //  이메일 아이디
    private String password;        // 비밀번호
    private String nickname;
    private URI profile;

    public UserModel() {    }         //파이어베이스 realtime database 를 사용할때 빈 생성자를 만들어 줘야해서 추가.

    public UserModel(String idToken, String emailId, String password, String nickname, URI profile) {
        this.idToken = idToken;
        this.emailId = emailId;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;

    }
//getter and setter 세팅
    public String getEmailId() {    return emailId;    }

    public void setEmailId(String emailId) {        this.emailId = emailId;    }

    public String getPassword() {        return password;    }

    public void setPassword(String password) {        this.password = password;    }

    public String getIdToken() {        return idToken;    }

    public void setIdToken(String idToken) {        this.idToken = idToken;    }

    public String getNickname() {        return nickname;    }

    public void setNickname(String nickname) {        this.nickname = nickname;    }

    public URI getProfile() {
        return profile;
    }

    public void setProfile(URI profile) {
        this.profile = profile;
    }
}
