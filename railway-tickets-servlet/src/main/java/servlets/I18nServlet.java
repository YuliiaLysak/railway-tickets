package servlets;

import exceptions.BusinessLogicException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

//@WebServlet(urlPatterns = "/i18n/*")
public class I18nServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(I18nServlet.class.getName());

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        String fileName = null;
        String[] pathParts = request.getPathInfo().split("/");
        long variablesCount = Arrays.stream(pathParts)
                .filter(variable -> !variable.isEmpty())
                .count();
        if (variablesCount == 1) {
            fileName = pathParts[1];
        }
        LOGGER.warning(fileName);
        URL resource = I18nServlet.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            LOGGER.warning("No resources file " + fileName);
            throw new BusinessLogicException("No resources file");
        }

        try {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(Files.readString(Paths.get(resource.toURI())));
            out.flush();
        } catch (URISyntaxException e) {
            LOGGER.warning(e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }
}
