package pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BookDaoImpl;
import dao.CustomerDaoImpl;
import pojos.Customer;

@WebServlet(value="/validate",loadOnStartup = 1)
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CustomerDaoImpl customerDao;
	private BookDaoImpl bookDao;

	public void init() throws ServletException {
		//create dao instance
		try {
			customerDao=new CustomerDaoImpl();
			bookDao=new BookDaoImpl();
		} catch (Exception e) {
			throw new ServletException("err in init : "+getClass().getName(), e);
		}
	}

	public void destroy() {
		try {
			customerDao.cleanUp();
			bookDao.cleanUp();
		} catch (SQLException e) {
			throw new RuntimeException("err in destroy : "+getClass().getName(), e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		try(PrintWriter pw=response.getWriter())
		{
			String email=request.getParameter("em");
			String password=request.getParameter("pass");
			System.out.println("in do-post "+email+" "+password);
			//invoke dao's method for authentication
			Customer customer = customerDao.authenticateUser(email, password);
			if(customer == null)
			{
				//failed login
				pw.print("<h5>Invalid Login , Please <a href='login.html'>Retry</a></h5>");
				
			}
			else {
				//get HttpSession (HS) from WC
				HttpSession session=request.getSession();
				System.out.println("from auth page session is new "+session.isNew());//true
				System.out.println("session id "+session.getId());
				
				session.setAttribute("user_details", customer);
			
				session.setAttribute("cust_dao", customerDao);
				session.setAttribute("book_dao", bookDao);
				
				session.setAttribute("shopping_cart",new ArrayList<Integer>());
				
				response.sendRedirect("category");
			}
			
			
		} catch (Exception e) {
			throw new ServletException("err in do-post : "+getClass().getName(), e);
		}
	}

}
