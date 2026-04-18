package com.SmartAttendance.demo.Security;

import com.SmartAttendance.demo.Entities.User;
import com.SmartAttendance.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public OAuthSuccessHandler(UserService userService,JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil=jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        System.out.println("OAuth success reached");

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name  = oauthUser.getAttribute("name");

        System.out.println("Email = " + email);
        System.out.println("Name = " + name);

        User user = userService.processOAuthUser(email, name);

        System.out.println("User found in DB");
        System.out.println("Status = " + user.getStatus());
        System.out.println("Role = " + user.getRole());

        if ("INCOMPLETE".equals(user.getStatus())) {

            String url =
                    "http://localhost:5173/complete-registration?email=" + email;

            System.out.println("Redirecting to: " + url);

            response.sendRedirect(url);

        } else {

            String role = user.getRole();
            Long id = user.getId();

            String token = jwtUtil.generateToken(id, email, role);
            System.out.println("Token = " + token);

            String url =
                    "http://localhost:5173/oauth/callback?token=" + token + "&role=" + role;

            System.out.println("Redirecting to: " + url);

            response.sendRedirect(url);
        }
    }
}