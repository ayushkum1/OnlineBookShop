package pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BookDaoImpl;
import pojos.Book;

/**
 * Servlet implementation class CategoryDetailsServlet
 */
@WebServlet("/category_details")
public class CategoryDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		try (PrintWriter pw = response.getWriter()) {
			String category = request.getParameter("cat");
			pw.print("<h4> Books Under Category " + category + "</h4>");
			
			HttpSession hs = request.getSession();
			
			BookDaoImpl bookDao = (BookDaoImpl) hs.getAttribute("book_dao");
			if (bookDao != null) {
				
				List<Book> bookList = bookDao.getBooksByCategory(category);
				// dynamic form generation
				pw.print("<form action='add_to_cart'>");
				for (Book b : bookList)
					pw.print("<input type='checkbox' name='bid' value=" + b.getBookId() + ">" + b + "<br>");
				pw.print("<input type='submit' value='Add To Cart'>");
				pw.print("</form>");
			} else
				pw.print("<h5> No Cookies : Session Tracking fails!!!!!!!</h5>");
		} catch (Exception e) {
			throw new ServletException("err in do-get : " + getClass().getName(), e);
		}
	}

}
