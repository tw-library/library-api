package com.thoughtworks.librarysystem.commons.config;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

@Component
public class AuthenticationFilter implements Filter {

    private static final String HEADER_PARAMETER_NAME_TOKEN = "x-token";
    public static final String VARIABLE_NAME = "AUTH_SECRET";
    private static final String AUTH_SECRET = System.getenv(VARIABLE_NAME) != null ? System.getenv(VARIABLE_NAME) :  "local_secret";

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        String method = request.getMethod();

        setCrossOriginResourceProperties(response);

        if(!method.equalsIgnoreCase(RequestMethod.OPTIONS.name())) {

            String token = request.getHeader(HEADER_PARAMETER_NAME_TOKEN);

            try {
                new JWTVerifier(AUTH_SECRET).verify(token);
            } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | JWTVerifyException | IllegalStateException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private void setCrossOriginResourceProperties(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PATCH");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, ".concat(HEADER_PARAMETER_NAME_TOKEN));
        response.setHeader("Access-Control-Expose-Headers", "Location");
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}

}