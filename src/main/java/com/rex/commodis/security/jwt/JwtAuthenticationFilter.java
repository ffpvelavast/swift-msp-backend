/** Servlet filter for JwtAuthentication; intercepts HTTP requests to apply cross-cutting behaviour before the controller. */
package com.rex.commodis.security.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rex.commodis.exceptions.UserNotProvisionedException;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.security.utils.SecurityConstants;
import com.rex.commodis.service.PlatformConfigurationService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final PlatformConfigurationService platformConfigurationService;

    private static final List<String> WHITELIST = List.of(
            "/register",
            "/login",
            "/api/register",
            "/api/login",
            "/api/translations/**",
            "/api/email-check",
            "/api/get-platform-by-email",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String path = request.getRequestURI();
        return WHITELIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        try {
            String header = request.getHeader(SecurityConstants.HEADER_STRING);

            if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                chain.doFilter(request, response);
                return;
            }

            String token = header.substring(SecurityConstants.TOKEN_PREFIX.length());

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication existingAuth = context.getAuthentication();

            if (existingAuth != null && !(existingAuth instanceof AnonymousAuthenticationToken)) {
                chain.doFilter(request, response);
                return;
            }

            if (!jwtTokenManager.validateToken(token)) {
                chain.doFilter(request, response);
                return;
            }

            Long platformId = jwtTokenManager.getPlatformId(token);
            String email    = jwtTokenManager.getEmail(token);
            Long rexUserId  = jwtTokenManager.getRexUserid(token); // throws UserNotProvisionedException if not found // quickfix for M2M token- not throwing error 

            RexUserContext rexUserContext = new RexUserContext(rexUserId, platformId, email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(rexUserContext, null, List.of());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authentication);

            log.debug("Authenticated user: email={}, rexUserId={}, platformId={}", email, rexUserId, platformId);

        } catch (UserNotProvisionedException ex) {
            log.warn("Access denied — user not provisioned: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    "{\"error\":\"" + ex.getMessage() + "\",\"status\":403}"
            );
            return;

        } catch (Exception ex) {
            log.error("JWT authentication failed: {}", ex.getMessage());
        }

        chain.doFilter(request, response);
    }
}
