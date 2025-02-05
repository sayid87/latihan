package com.latihan.latihan.requests;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRequest {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterRequest {
        private String email;
        private String password;
        private String ulangi;
        private String nama;
        private String telp;
        private MultipartFile foto;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateRequest {
        private String nama;
        private String telp;
        private MultipartFile foto;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest {
        private String email;
        private String password;
    }
}
