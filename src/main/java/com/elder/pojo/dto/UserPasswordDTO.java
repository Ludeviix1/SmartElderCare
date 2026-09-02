package com.elder.pojo.dto;

import lombok.Data;

@Data
public class UserPasswordDTO {
    private String oldPassword;
    private String newPassword;
}
