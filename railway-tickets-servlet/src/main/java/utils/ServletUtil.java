package utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public final class ServletUtil {

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

    public static void sendSuccessResponse(HttpServletResponse response, Object content) throws IOException {
        String jsonContent = ServiceLocator.getGson().toJson(content);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonContent);
        out.flush();
    }

    private ServletUtil() {
    }
}
