package com.cinema.util;

import com.cinema.exception.AccessDeniedException;
import com.cinema.model.User;
import com.cinema.model.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginLevelCheckUtil {

    private static final Logger log = LoggerFactory.getLogger(LoginLevelCheckUtil.class);

    public static User getCurrentUser(Authentication auth) {
        return (User) auth.getPrincipal();
    }

    private static void requireAdminOrSuperAdmin(User currentUser) {
        if (currentUser.getRole() != Role.ADMIN
                && currentUser.getRole() != Role.SUPER_ADMIN) {
            log.warn("Access denied: userId={}, role={}, requiredRoles=[ADMIN,SUPER_ADMIN]",
                    currentUser.getId(),
                    currentUser.getRole());

            throw new AccessDeniedException();
        }
    }

    public static void requireAdminOrSuperAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        requireAdminOrSuperAdmin(user);
    }

}
