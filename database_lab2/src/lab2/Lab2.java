package lab2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lab2 {
	Connection con = null;
	
	public Lab2(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/COMPANY?useSSL=false&serverTimezone = GMT&allowPublicKeyRetrieval=true","root","123456");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public List<String> inputHandler(String input){
		List<String> result = new ArrayList<String>();
		String reg = "company_query -q ([1-9]) -p ([a-zA-Z0-9 ]+)";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(input);
		if(m.find()){
			result.add(m.group(1));
			String[] word = m.group(2).split(" ");
			result.addAll(Arrays.asList(word));
		}
		return result;
	}
	
	public void mysqlHandler(List<String> words){
		String input = "";
		switch(words.get(0)){
		case "1":
			input += "select employee.essn "
					+ "from employee,project,works_on "
					+ "where employee.essn=works_on.essn and project.pno=works_on.pno and project.pno='" + words.get(1) + "';";
			System.out.println(input);
			break;
		case "2":
			input += "select ename "
					+ "from employee,project,works_on "
					+ "where employee.essn=works_on.essn and project.pno=works_on.pno and pname='" + words.get(1) + "';";
			break;
		case "3":
			input += "select ename,address "
					+ "from department,employee "
					+ "where employee.dno=department.dno and dname='" + words.get(1) + "';";
			break;
		case "4":
			input += "select ename,address "
					+ "from employee,department "
					+ "where employee.dno=department.dno and dname='" + words.get(1) + "' and salary<"+words.get(2) + ";";
			break;
		case "5":
			input += "select ename "
					+ "from employee "
					+ "where essn not in ("
					+ "select employee.essn "
					+ "from employee,works_on "
					+ "where employee.essn=works_on.essn and pno='" + words.get(1) + "');";
			break;
		case "6":
			input += "select ename,dname "
					+ "from employee,department "
					+ "where employee.dno=department.dno and superssn in ("
					+ "select essn "
					+ "from employee "
					+ "where employee.ename = '" + words.get(1) + "';";
			break;
		case "7":
			input += "select employee.essn "
					+ "from emoloyee,works_on "
					+ "where employee.essn=works_on.essn and works_on.pno='" + words.get(1) + "' and employee.essn in ("
					+ "select employee.essn "
					+ "from employee,works_on "
					+ "where employee.essn=works_on.essn and works_on.pno='" + words.get(2) + "');";
			break;
		case "8":
			input += "select department.dname "
					+ "from employee,department "
					+ "where employee.dno = department.dno "
					+ "group by dno having avg(salary)<" + words.get(1) + ";";
			break;
		case "9":
			input += "select ename "
					+ "from employee,works_on "
					+ "where employee.essn=works_on.essn "
					+ "group by pno having count(*) >=" + words.get(1) + " and sum(hours)<=" + words.get(2) + ";";
			break;
		default:
			System.out.println("input error");
			break;
		}
		try {
			PreparedStatement ps = this.con.prepareStatement(input);
			ResultSet rs = ps.executeQuery();
			int count = rs.getMetaData().getColumnCount();
			while(rs.next()){
				for(int i=1;i<=count;i++){
					System.out.print(rs.getString(i));
					if(i<count){
						System.out.print(",");
					}
				}
				System.out.println();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String args[]){
		Lab2 test = new Lab2();
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		while(!input.equals("c")){
			test.mysqlHandler(test.inputHandler(input));
			input = in.nextLine();
		}
	}
}
