package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test {
	public static void main(String args[]){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("successfully");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/COMPANY?useSSL=false","root","123456");
			System.out.println("successfully again");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addEmployee(String name,String essn, String address, int salary, String superssn,int dno){
		String sql = "insert into company values('" + name + "','" + essn + "','" + address + "','" + salary + "','"
				 + superssn + "','" + dno + "')";
		
	}
}
