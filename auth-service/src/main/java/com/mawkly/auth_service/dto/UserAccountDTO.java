package com.mawkly.auth_service.dto;

import com.mawkly.auth_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO {
    private Long id;
    private String username;
    private Role role; //

}
