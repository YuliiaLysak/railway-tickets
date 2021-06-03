package servlets;

import exceptions.BusinessLogicException;

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

/**
 * Used to provide i18n by locale for JavaScript files using urlPattern "/i18n/*"
 *
 * @author Yuliia Lysak
 */
public class I18nServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(I18nServlet.class.getName());

    /**
     * Returns content of resource bundle file as text/plain.
     * Sends response status code 404 if file name not present or resource file was not found
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String[] pathParts = request.getPathInfo().split("/");
        long variablesCount = Arrays.stream(pathParts)
                .filter(variable -> !variable.isEmpty())
                .count();

        String fileName = variablesCount == 1 ? pathParts[1] : null;
        if (fileName == null) {
            LOGGER.warning("File name not present");
            response.setStatus(404);
            return;
        }

        URL resource = I18nServlet.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            LOGGER.warning("No resources file " + fileName);
            response.setStatus(404);
            return;
        }

        try {
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.print(Files.readString(Paths.get(resource.toURI())));
            out.flush();
        } catch (URISyntaxException e) {
            LOGGER.warning(e.getMessage());
            throw new BusinessLogicException(e.getMessage());
        }
    }
}
