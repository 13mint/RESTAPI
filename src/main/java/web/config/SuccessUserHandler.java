package web.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SuccessUserHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        var authorities = authentication.getAuthorities();

        if (authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            response.sendRedirect("/admin");

        } else {
            response.sendRedirect("/user");
        }
    }
}