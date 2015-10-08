package com.thoughtworks.librarysystem.commons.config;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.auth0.jwt.internal.org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

/**
 *  CORS: Cross-origin resource sharing: is a mechanism that allows restricted resources (e.g. fonts, JavaScript, etc.)
 *  on a web page to be requested from another domain outside the domain from which the resource originated.
**/
@Component
public class AuthenticationFilter implements Filter {

    private static final String HEADER_PARAMETER_NAME_TOKEN = "x-token";

    private static final String SUPER_SECRET = "superSecret";

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;

        HttpServletRequest request = (HttpServletRequest) req;

        String method = request.getMethod();

        settingCORSProperties(response);

        if(!method.equalsIgnoreCase(RequestMethod.OPTIONS.name())) {

            String token = request.getHeader(HEADER_PARAMETER_NAME_TOKEN);

            try {
                new JWTVerifier(SUPER_SECRET).verify(token);
            } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | JWTVerifyException | IllegalStateException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private void settingCORSProperties(HttpServletResponse response) {
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