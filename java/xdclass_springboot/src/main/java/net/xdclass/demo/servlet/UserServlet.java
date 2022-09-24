package net.xdclass.demo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "userServlet",urlPatterns = "/v1/api/test/customs")
public class UserServlet extends HttpServlet{

	 @Override
     public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 
		 resp.getWriter().print("custom sevlet");
         resp.getWriter().flush();
         resp.getWriter().close();
     }

	 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

	 
    
}
