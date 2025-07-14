package com.busticket.user_service.config;

import com.busticket.user_service.entity.Role;
import com.busticket.user_service.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        List<String> roles = List.of("ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_BUS_OPERATOR");

        for (String roleName : roles) {
            if (!roleRepository.existsByName(roleName)) {
                roleRepository.save(Role.builder().name(roleName).build());
            }
        }
    }
}

