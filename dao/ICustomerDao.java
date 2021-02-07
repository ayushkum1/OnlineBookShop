package dao;

import java.sql.SQLException;

import pojos.Customer;

public interface ICustomerDao {
	Customer authenticateUser(String email,String pwd) throws SQLException;
}
