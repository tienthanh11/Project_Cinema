package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.entity.Membership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AuthService {

//    public boolean isAuthenticated() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null && authentication.isAuthenticated()
//                && !"anonymousUser".equals(authentication.getPrincipal());
//    }
//
//    public Membership getCurrentMembership() {
//        if (!isAuthenticated()) {
//            return null;
//        }
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//        String username;
//
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//
//        Membership membership = memberShipService.findByUsername(username);
//        if (membership == null) {
//            throw new RuntimeException("Membership not found for user: " + username);
//        }
//
//        return membership;
//    }

//    public void addAuthAttributes(Model model, Authentication authentication) {
//        boolean isLoggedIn = isAuthenticated();
//
//        if (isLoggedIn) {
//            String username = authentication.getName();
//            boolean isAdmin = authentication.getAuthorities().stream()
//                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
//            Membership membership = getCurrentMembership();
//
//            model.addAttribute("username", username);
//            model.addAttribute("isAdmin", isAdmin);
//            model.addAttribute("membership", membership);
//            model.addAttribute("isAuthenticated", true);
//        } else {
//            model.addAttribute("username", null);
//            model.addAttribute("isAdmin", false);
//            model.addAttribute("membership", null);
//            model.addAttribute("isAuthenticated", false);
//        }
//    }


    public void addAuthAttributes(Model model, Authentication authentication) {
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());

        if (isLoggedIn) {
            String username = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        } else {
            model.addAttribute("username", null);
            model.addAttribute("isAdmin", false);
        }
    }
}
