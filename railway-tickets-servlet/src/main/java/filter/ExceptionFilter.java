package filter;

import utils.ServletUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

public class ExceptionFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain
    ) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            chain.doFilter(request, response);
        } catch (Exception exception) {
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
        return ResourceBundle.getBundle("messages", locale, ServletUtil.class.getClassLoader())
                .getString(messageKey);
    }

    // TODO - add filter encoding

    // TODO - add ServletListener

    // TODO - Create own custom tags

    // TODO - Add pagination

    // TODO - change logger to log4j
}
