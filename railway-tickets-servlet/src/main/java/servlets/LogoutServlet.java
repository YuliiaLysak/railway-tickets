package servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used to process log out of users from the application by urlPattern "/logout"
 *
 * @author Yuliia Lysak
 */
public class LogoutServlet extends HttpServlet {

    /**
     * Invalidate session of authorized user
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        request.getSession().invalidate();

        response.sendRedirect(request.getContextPath() + "/");
    }
}
