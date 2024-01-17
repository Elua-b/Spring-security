package com.eloi.example.v1.utils;

import com.eloi.example.v1.security.user.UserSecurityDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserUtils {
    public static boolean isUserLoggedIn(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return true;
        }else{
              return false;
        }
    }
    public static UserSecurityDetails getLoggedInUser() {
        // Retrieve the currently authenticated user from the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return (UserSecurityDetails) authentication.getPrincipal();
        }
        // Return null or handle the case when no user is authenticated
        return null;
    }
}
