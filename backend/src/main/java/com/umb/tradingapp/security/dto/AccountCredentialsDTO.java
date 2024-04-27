package com.umb.tradingapp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCredentialsDTO {
    private String username;
    private String password;
}
