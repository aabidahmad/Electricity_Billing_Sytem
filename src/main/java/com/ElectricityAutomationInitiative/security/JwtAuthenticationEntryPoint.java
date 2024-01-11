package com.ElectricityAutomationInitiative.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * JwtAuthenticationEntryPoint is a component used in Spring Security to handle unauthorized access to
 * protected resources.
 * When a user attempts to access a protected resource without proper authentication or with invalid
 * credentials,
 * the JwtAuthenticationEntryPoint will be triggered to handle the situation.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint,Serializable{
    private static final long serialVersionUID = -7858869558953243875L;

    /**
     * This method is called whenever an unauthorized request is made to a protected resource.
     * It is responsible for sending an error response with the appropriate HTTP status code (401 Unauthorized)
     * along with a custom message explaining the reason for the unauthorized access.
     *
     * @param request       The HttpServletRequest that resulted in an AuthenticationException.
     * @param response      The HttpServletResponse to which the error response should be sent.
     * @param authException The AuthenticationException that caused the unauthorized access.
     * @throws IOException      If an input or output exception occurs while sending the error response.
     * @throws ServletException If a general servlet exception occurs.
     */

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Access ");
        }
    }
