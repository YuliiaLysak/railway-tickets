package filter;

import config.SecurityConfig;
import model.Role;
import model.SessionUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class AuthenticationFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        LOGGER.warning("Entered AuthenticationFilter from " + request.getRequestURI());
//        LOGGER.warning("getRequestURL = " + request.getRequestURL());
//        LOGGER.warning("getServletPath = " + request.getServletPath());
//        LOGGER.warning("getRequestURI = " + request.getRequestURI());
//        LOGGER.warning("getPathInfo = " + request.getPathInfo());
//        LOGGER.warning("getContextPath = " + request.getContextPath());
//        LOGGER.warning("getQueryString = " + request.getQueryString());
//        LOGGER.warning("getHttpServletMapping = " + request.getHttpServletMapping().getPattern());


        if (isPermittedForEveryone(request)) {
            LOGGER.warning("Page is permitted for everyone");
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            LOGGER.warning("Authentication required");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (hasPermission(request, sessionUser.getRoles())) {
            LOGGER.warning("Authorized role for " + sessionUser.getId());
            filterChain.doFilter(request, response);
            return;
        }

        // TODO - add http403.jsp file
        LOGGER.warning("Access denied!");
        response.setStatus(403);
        request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/http403.jsp")
                .forward(request, response);
    }

    // Check whether this 'request' is required to login or not.
    private boolean isPermittedForEveryone(HttpServletRequest request) {
        String url = request.getHttpServletMapping().getPattern();
        Set<Role> roles = SecurityConfig.getAllAppRoles();

        for (Role role : roles) {
            List<String> urlPatterns = SecurityConfig.getUrlPatternsForRole(role);
            if (urlPatterns != null && urlPatterns.contains(url)) {
                return false;
            }
        }
        return true;
    }

    // Check if this 'request' has a 'valid role'?
    private boolean hasPermission(HttpServletRequest request, Set<Role> roles) {
        String url = request.getHttpServletMapping().getPattern();

        for (Role role : roles) {
            List<String> urlPatterns = SecurityConfig.getUrlPatternsForRole(role);
            if (urlPatterns != null && urlPatterns.contains(url)) {
                return true;
            }
        }
        return false;
    }
}
