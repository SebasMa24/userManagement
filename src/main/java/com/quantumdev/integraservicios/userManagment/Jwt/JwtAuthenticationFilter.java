package com.quantumdev.integraservicios.userManagment.Jwt;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quantumdev.integraservicios.userManagment.Service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String token = getTokenFromRequest(request);
            final String username;

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            username = jwtService.getEmailFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.IsTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            handleJwtException(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "El token JWT ha expirado. Por favor, inicia sesión nuevamente.");
        } catch (Exception ex) {
            handleJwtException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error interno al procesar el token JWT.");
        }
    }

    private String getTokenFromRequest(HttpServletRequest request){
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        if(org.springframework.util.StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")){
            return authHeader.substring(7);
        }
        return null;
    }

    private void handleJwtException(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().toString());
        errorBody.put("status", status);
        errorBody.put("error", HttpStatus.valueOf(status).getReasonPhrase());
        errorBody.put("message", message);

        new ObjectMapper().writeValue(response.getWriter(), errorBody);
    }
}
