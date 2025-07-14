
package com.busticket.user_service.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private boolean emailVerified;
    private boolean active;
    private Set<String> roles;
}