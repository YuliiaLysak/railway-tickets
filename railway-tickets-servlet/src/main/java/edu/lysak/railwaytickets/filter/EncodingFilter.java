package edu.lysak.railwaytickets.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter for providing request and response encoding in UTF-8
 *
 * @author Yuliia Lysak
 */
public class EncodingFilter implements Filter {
    private static final String ENCODING = "UTF-8";

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain
    ) throws ServletException, IOException {

        servletRequest.setCharacterEncoding(ENCODING);
        servletResponse.setCharacterEncoding(ENCODING);

        chain.doFilter(servletRequest, servletResponse);
    }
}
