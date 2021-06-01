package filter;

import utils.ServletUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionFilter implements Filter {
    // TODO - change logger to log4j
    private static final Logger LOGGER = Logger.getLogger(ExceptionFilter.class.getName());

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain
    ) throws IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            chain.doFilter(request, response);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "unhandled exception", exception);

            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(409);
            PrintWriter out = response.getWriter();

            HttpSession session = request.getSession();
            String lang = (String) session.getAttribute("locale");
            Locale locale = Locale.forLanguageTag(lang);
            String messageKey = exception.getMessage();
            out.print(getMessageForLocale(messageKey, locale));
            out.flush();
        }
    }

    private String getMessageForLocale(String messageKey, Locale locale) {
        LOGGER.warning("Handling localized exception with message '" + messageKey + "'");
        if (messageKey == null) {
            return "";
        }
        return ResourceBundle.getBundle("messages", locale, ServletUtil.class.getClassLoader())
                .getString(messageKey);
    }
}
