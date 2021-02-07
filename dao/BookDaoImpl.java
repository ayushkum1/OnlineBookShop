package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pojos.Book;

import static utils.DBUtils.fetchDBConnection;

public class BookDaoImpl implements IBookDao {
	private Connection cn;
	private PreparedStatement pst1, pst2,pst3;

	public BookDaoImpl() throws ClassNotFoundException, SQLException {
		// get fixed cn from DBUtils
		cn = fetchDBConnection();
		pst1 = cn.prepareStatement("select distinct category from dac_books");
		pst2 = cn.prepareStatement("select * from dac_books where category=?");
		pst3=cn.prepareStatement("select * from dac_books where id=?");
		System.out.println("book dao created....");
	}

	@Override
	public List<String> getAllCategories() throws SQLException {
		List<String> categoryNames = new ArrayList<>();
		try (ResultSet rst = pst1.executeQuery()) {
			while (rst.next())
				categoryNames.add(rst.getString(1));
		}
		return categoryNames;
	}

	@Override
	public List<Book> getBooksByCategory(String categoryName) throws SQLException {
		List<Book> books = new ArrayList<>();
		// set IN param : category name
		pst2.setString(1, categoryName);
		try (ResultSet rst = pst2.executeQuery()) {
			while (rst.next())
				books.add(new Book(rst.getInt(1), rst.getString(2), 
						rst.getString(3), categoryName, rst.getDouble(5)));
		}
		return books;
	}
	

	@Override
	public Book getBookDetailsById(int bookId) throws SQLException {
		//set IN param : book id
		pst3.setInt(1, bookId);
		try(ResultSet rst=pst3.executeQuery())
		{
			if(rst.next())
				return new Book(bookId, rst.getString(2), 
						rst.getString(3), rst.getString(4), rst.getDouble(5));
		}
		return null;
	}

	public void cleanUp() throws SQLException {
		if (pst1 != null)
			pst1.close();
		if (pst2 != null)
			pst2.close();
		if (pst3 != null)
			pst3.close();
		if (cn != null)
			cn.close();
		System.out.println("book dao closed");
	}

}
