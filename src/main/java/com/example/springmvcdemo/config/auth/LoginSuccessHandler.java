package com.example.springmvcdemo.config.auth;

import com.example.springmvcdemo.model.User;
import com.example.springmvcdemo.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.springmvcdemo.utils.Utils.*;

@RequiredArgsConstructor
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);

        System.out.println("Inside login success handler");
        request.getSession().setAttribute(USER_ID, user.getId());
        request.getSession().setAttribute(USER_FIRST_NAME, user.getFirstName());
        request.getSession().setAttribute(USER_LAST_NAME, user.getLastName());

        redirectStrategy.sendRedirect(request, response, "/");

    }
}
