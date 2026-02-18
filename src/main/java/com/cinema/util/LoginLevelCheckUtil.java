package com.cinema.util;

import com.cinema.model.User;
import com.cinema.model.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class LoginLevelCheckUtil {

    public static User getCurrentUser(Authentication auth) {
        return (User) auth.getPrincipal();
    }

    private static void requireAdminOrSuperAdmin(User currentUser) {
        if(currentUser.getRole() != Role.ADMIN ||
                currentUser.getRole() != Role.SUPER_ADMIN) {
            throw new RuntimeException("Access denied");
        }
    }

    public static void requireAdminOrSuperAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        requireAdminOrSuperAdmin(user);
    }

}
