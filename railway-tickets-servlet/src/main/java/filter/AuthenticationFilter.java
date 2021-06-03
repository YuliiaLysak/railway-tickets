package filter;

import exceptions.BusinessLogicException;
import listener.AnalyticSessionListener;
import model.Role;
import model.SessionAnalytic;
import model.SessionUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Filter for providing security of the application
 *
 * @author Yuliia Lysak
 */
public class AuthenticationFilter implements Filter {

    //    TODO - double check if other pages needed to be secured with authorization
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

        if (hasPermission(request, Set.of())) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            String redirectAfterLogin = request.getRequestURI();
            session.setAttribute("redirectAfterLogin", redirectAfterLogin);
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (hasPermission(request, sessionUser.getRoles())) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(403);
        request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/errors/403forbidden.jsp")
                .forward(request, response);
    }

    /**
     * Checks if user has permission to see requested page
     *
     * @param request - the request from the client side
     * @param roles - set of user roles
     *
     * @return true if user has permission to see requested page otherwise false
     */
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
