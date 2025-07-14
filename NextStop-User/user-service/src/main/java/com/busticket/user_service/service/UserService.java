
package com.busticket.user_service.service;

import com.busticket.user_service.dto.UserDto;
import com.busticket.user_service.dto.UserUpdateRequest;
import com.busticket.user_service.entity.Role;
import com.busticket.user_service.entity.User;
import com.busticket.user_service.exception.UserServiceException;
import com.busticket.user_service.repository.RoleRepository;
import com.busticket.user_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserDto getUserById(Long id) {
        User user = userRepository.findByIdWithRoles(id)
                //.filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return mapToDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAllWithRoles();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFullName(dto.getFullName());
        if (!user.getEmail().equals(dto.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new UserServiceException("Email already registered");
            }
            user.setEmail(dto.getEmail());
            user.setEmailVerified(false); // Require re-verification
        }
        user.setActive(dto.isActive());

        User updated = userRepository.save(user);
        return mapToDto(updated);
    }

    @Transactional
    public UserDto updateUserBySelf(UserUpdateRequest request) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailWithRoles(currentEmail)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFullName(request.getFullName());
        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserServiceException("Email already registered");
            }
            user.setEmail(request.getEmail());
            user.setEmailVerified(false); // Require re-verification
        }

        User updated = userRepository.save(user);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findByIdWithRoles(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.isEmailVerified(),
                user.isActive(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }

    public UserDto updateUserRoles(Long id, Set<String> roleNames) {
        User user = userRepository.findByIdWithRoles(id)
                .filter(User::isActive)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Set<Role> roles = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new EntityNotFoundException("Role not found: " + name)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        User updated = userRepository.save(user);
        return mapToDto(updated);
    }

    @Transactional
    public UserDto restoreUser(Long id) {
        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setActive(true);
        User restored = userRepository.save(user);
        return mapToDto(restored);
    }
}
