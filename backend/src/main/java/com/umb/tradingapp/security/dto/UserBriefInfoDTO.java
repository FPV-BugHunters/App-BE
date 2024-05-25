package com.umb.tradingapp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBriefInfoDTO
{
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;

}
