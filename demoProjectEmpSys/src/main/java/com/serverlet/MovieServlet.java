package com.serverlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MovieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/home".equals(path)) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else if (("/movieDetails".equals(path))) {
            request.getRequestDispatcher("/movieDetails.jsp").forward(request, response);
        }
    }
}
