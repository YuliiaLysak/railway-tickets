package edu.lysak.railwaytickets.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Utility class for servlets
 *
 * @author Yuliia Lysak
 */
public final class ServletUtil {

    /**
     * Gets path variable from url-pattern if present
     *
     * @param path a pathInfo String
     * @return path variable Long or null
     */
    public static Long getPathVariable(String path) {
        if (path == null) {
            return null;
        }
        String[] pathParts = path.split("/");
        long variablesCount = Arrays.stream(pathParts)
                .filter(variable -> !variable.isEmpty())
                .count();
        if (variablesCount != 1) {
            return null;
        }
        try {
            return Long.parseLong(pathParts[1]);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * Converts object to Json and write it to output channel of response
     *
     * @param response - the response which will be sent from the server side
     * @param content - an object with content for response
     */
    public static void sendSuccessResponse(
            HttpServletResponse response,
            Object content
    ) throws IOException {
        String jsonContent = ServiceLocator.getGson().toJson(content);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonContent);
        out.flush();
    }

    private ServletUtil() {
    }
}
