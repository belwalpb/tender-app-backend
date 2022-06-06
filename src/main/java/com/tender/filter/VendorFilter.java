package com.tender.filter;

import com.tender.constants.TenderConstants;
import com.tender.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class VendorFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(VendorFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            log.info("Filter Invoked for Role : {}", TenderConstants.VENDOR_ROLE);
            String jwt = parseJwt(request);
            if(Objects.isNull(jwt) || (!JwtUtils.validateJwtTokenWithRole(jwt, TenderConstants.VENDOR_ROLE))) {
                response.sendError(401, "Unauthorized");
                return;
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
       filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        if (Objects.nonNull(authToken) && authToken.startsWith("Bearer ")) {
            return authToken.substring(7, authToken.length());
        }
        return null;
    }
}
