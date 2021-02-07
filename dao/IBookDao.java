package dao;

import java.sql.SQLException;
import java.util.List;

import pojos.Book;

public interface IBookDao {
//add a method for getting all categories from the books table
	List<String> getAllCategories() throws SQLException;
	//add a method for getting all available book under specific category
	List<Book> getBooksByCategory(String categoryName)  throws SQLException;
	//add a method for getting book details by supplied id
	Book getBookDetailsById(int bookId) throws SQLException;
	
}
