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

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name  = oauthUser.getAttribute("name");

        User user = userService.processOAuthUser(email, name);

        if ("INCOMPLETE".equals(user.getStatus())) {
            // 🆕 New user → go to registration page
            response.setContentType("application/json");
            response.sendRedirect(
                    "http://localhost:5173/complete-registration?email=" + email
            );
        } else {
            // ✅ Existing user → go to dashboard
            // we need to generate JWT token for this existing user
            String role=user.getRole();
            Long id=user.getId();
            String token=jwtUtil.generateToken(id,email,role);
            //sending the token via JSON
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"token\": \"" + token + "\", \"role\": \"" + user.getRole() + "\"}"
            );
        }
    }
}