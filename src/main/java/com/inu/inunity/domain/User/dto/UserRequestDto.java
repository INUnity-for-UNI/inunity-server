package com.inu.inunity.domain.User.dto;

public class UserRequestDto {
    public static class updateUser{
        private String nickName;
        private Boolean isGraduation;
    }

    public static class setUser{
        private String userName;
        private String nickName;
        private String major;
    }
}
