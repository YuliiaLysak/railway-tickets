package filter;

import model.Role;
import model.SessionUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class AuthenticationFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());

    //    TODO - check if other pages needed to be secured with authorization
    private static final Map<String, Set<Role>> mapConfig = Map.of(
            "/api/tickets", Set.of(Role.USER),
            "/admin/*", Set.of(Role.ADMIN),
            "/api/routes/*", Set.of(Role.ADMIN),
            "/api/stations/*", Set.of(Role.ADMIN)
    );

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


        if (hasPermission(request, Set.of())) {
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

    private boolean hasPermission(HttpServletRequest request, Set<Role> roles) {
        String url = request.getHttpServletMapping().getPattern();
        Set<Role> requiredRoles = getRequiredRoles(url);

        if (requiredRoles == null || requiredRoles.isEmpty()) {
            return true;
        }

        for (Role role : roles) {
            if (requiredRoles.contains(role)) {
                return true;
            }
        }

        return false;
    }

    private Set<Role> getRequiredRoles(String url) {
        return mapConfig.get(url);
    }
}
