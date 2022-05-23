package com.robot.homeobot.auth;

import com.robot.homeobot.model.JwtBlacklist;
import com.robot.homeobot.repository.JwtBlacklistRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTFilter extends GenericFilterBean {
    @Value("somesecret")
    public String SECRET;
    @Autowired
    public JwtBlacklistRepository jwtBlacklistRepository;

    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        final String authHeader = request.getHeader("authorization");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            chain.doFilter(req, res);
        } else {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ServletException("Missing or invalid Authorization header");
            }

            final String token = authHeader.substring(7);
            JwtBlacklist blacklist = this.jwtBlacklistRepository.findByTokenEquals(token);

            if(blacklist == null) {
                final Claims claims = Jwts.parser().setSigningKey("topsecretjwtpass".getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
                request.setAttribute("claims", claims);
            } else {
                throw new ServletException("Invalid token." + "");

            }

            chain.doFilter(req, res);
        }

    }
}