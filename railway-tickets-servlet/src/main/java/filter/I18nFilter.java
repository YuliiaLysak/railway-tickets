package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class I18nFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain
    ) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        String currentLocale = (String) session.getAttribute("locale");
        if (currentLocale == null) {
            currentLocale = "en";
        }

        String lang = request.getParameter("lang");
        if (lang != null && !lang.isEmpty() && ("en".equals(lang) || "ua".equals(lang))) {
            currentLocale = lang;
        }

        session.setAttribute("locale", currentLocale);

        chain.doFilter(request, response);
    }
}
