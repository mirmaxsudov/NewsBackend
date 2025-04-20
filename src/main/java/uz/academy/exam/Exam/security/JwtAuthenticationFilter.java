package uz.academy.exam.Exam.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.academy.exam.Exam.security.service.CustomUserDetailsService;
import uz.academy.exam.Exam.security.service.JwtService;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1) Get Authorization header
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        // 2) If header is present and starts with "Bearer ", extract token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7).trim();  // remove "Bearer "
            if (!jwtToken.isEmpty()) {
                // 3) Extract username (subject) from token
                try {
                    username = jwtService.extractUsername(jwtToken);
                } catch (Exception ex) {
                    // Invalid token format, skip setting SecurityContext
                }
            }
        }

        // 4) If we got a username and no authentication yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 5) Load user details
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // 6) Validate token
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                // 7) Build authentication object
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                // 8) Set into SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 9) Continue filter chain
        filterChain.doFilter(request, response);
    }
}