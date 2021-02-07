package dao;

import java.sql.*;
import static utils.DBUtils.fetchDBConnection;

import pojos.Customer;

public class CustomerDaoImpl implements ICustomerDao {
	private Connection cn;
	private PreparedStatement pst1;

	public CustomerDaoImpl() throws ClassNotFoundException, SQLException {
		// get fixed db cn from db utils
		cn = fetchDBConnection();
		// pst1 : auth
		pst1 = cn.prepareStatement("select * from my_customers where email=? and password=?");
		System.out.println("customer dao created");
	}

	@Override
	public Customer authenticateUser(String email, String pwd) throws SQLException {
		// set IN params
		pst1.setString(1, email);
		pst1.setString(2, pwd);
		// exec method : exec query
		try (ResultSet rst = pst1.executeQuery()) {
			if (rst.next())
				return new Customer(rst.getInt(1), rst.getDouble(2), 
						email, rst.getString(4), pwd, rst.getDate(6),
						rst.getString(7));
		}
		return null;
	}

	public void cleanUp() throws SQLException {
		if (pst1 != null)
			pst1.close();
		if (cn != null)
			cn.close();
		System.out.println("cust dao cleaned up...");
	}

}
