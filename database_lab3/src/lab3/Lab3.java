package lab3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Lab3 {
	Connection con = null;

	public Lab3(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dormitory?useSSL=false&serverTimezone = GMT&allowPublicKeyRetrieval=true","root","123456");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void show_menu(){
		Scanner s = new Scanner(System.in);
		this.show_main_menu();
		int choice1 = s.nextInt();
		s.nextLine();
		switch(choice1){
		case 1:
			System.out.println("1.通过学生姓名查询学生信息");
			System.out.println("2.通过教师姓名查找其指导的学生");
			System.out.println("3.查询某公寓检查评价情况");
			System.out.println("4.查询每个学生选修的课程的数量");
			System.out.println("5.查询没有选择某门课的学生姓名");
			String choice2 = s.nextLine();
			this.inquire(choice2);
			break;
		case 2:
			System.out.println("1.通过学号删除学生");
			System.out.println("2.删除某学生的某条选课记录");
			System.out.println("3.删除某天对某公寓的检查");
			String choice3 = s.nextLine();
			this.delete(choice3);
			break;
		case 3:
			System.out.println("1.添加某学生选修某课程");
			System.out.println("2.添加一次公寓检查记录");
			System.out.println("3.为某学生更换指导老师");
			System.out.println("4.添加学生");
			String choice4 = s.nextLine();
			this.insert(choice4);
			break;
		case 0:
			break;
		default:
			System.out.println("无法识别的输入！");
			break;
		}
	}
	
	public void inquire(String choice){
		Scanner in = new Scanner(System.in);
		String input = "";
		int flag = 0;
		while(flag == 0){
			switch(choice){
			case "1":
				System.out.print("输入要查询学生的姓名：");
				String input1 = in.nextLine();
				input = "select * "
						+ "from student "
						+ "where name = '" + input + "';";
				choice = in.nextLine();
				break;
			case "2":
				System.out.print("输入要查询的教师的姓名：");
				String input2 = in.nextLine();
				input = "select student.name "
						+ "from student,mentor "
						+ "where student.mno = mentor.mno and mentor.name = '" + input + "';";
				choice = in.nextLine();
				break;
			case "3":
				System.out.print("输入要查询的公寓的名字：");
				String input3 = in.nextLine();
				input = "select check_data,evaluation "
						+ "from checks "
						+ "where checke.dorm_name= '" + input3 + "';";
				choice = in.nextLine();
				break;
			case "4":
				input = "select student.sno,count(*) "
						+ "from student,elective_list "
						+ "where student.sno = elective_list.sno "
						+ "group by student.sno";
				choice = in.nextLine();
				break;
			case "5":
				System.out.println("输入课程的号：");
				String input5 = in.nextLine();
				input = "select student.name "
						+ "from student "
						+ "where cno not in ("
						+ "select student.cno "
						+ "from student,elective_list "
						+ "where student.cno=elective_list.cno and elective.sno = '" + input5 + "');";
				choice = in.nextLine();
				break;
			case "..":
				this.show_menu();
				flag = 1;
				break;
			default:
				System.out.println("无法识别的输入！");
				choice = in.nextLine();
				break;
			}
		}
	}
	
	public void delete(String choice){
		Scanner in = new Scanner(System.in);
		String input = "";
		int flag = 0;
		while(flag == 0){
			switch(choice){
			case "1":
				System.out.print("输入要删除学生的学号：");
				String input1 = in.nextLine();
				input = "delete "
						+ "from student "
						+ "where student.sno = '" + input + "';";
				choice = in.nextLine();
				break;
			case "2":
				System.out.print("输入要删除的学生学号和课程号：");
				String input2 = in.nextLine();
				String[] words2 = input.split(" ");
				input = "delete "
						+ "from elective_list "
						+ "where sno = '" + words2[0] + "' and cno = '" + words2[1] + "';";
				choice = in.nextLine();
				break;
			case "3":
				System.out.print("输入日期和公寓名：");
				String input3 = in.nextLine();
				String[] words3 = input.split(" ");
				input = "delete "
						+ "from checks "
						+ "where data = '" + words3[0] + "' and dorm_name = '" + words3[1] + "';";
				choice = in.nextLine();
				break;
			case "..":
				this.show_menu();
				flag = 1;
				break;
			default:
				System.out.println("无法识别的输入！");
				choice = in.nextLine();
				break;
			}
		}
	}
	
	public void insert(String choice){
		Scanner in = new Scanner(System.in);
		String input = "";
		int flag = 0;
		while(flag == 0){
			switch(choice){
			case "1":
				System.out.print("输入学生学号和课程号：");
				String input1 = in.nextLine();
				String[] words1 = input1.split(" ");
				input = "insert "
						+ "into elective_list "
						+ "values('" + words1[0] + "','" + words1[1] + "');";
				choice = in.nextLine();
				break;
			case "2":
				System.out.println("请依次输入检查员编号，公寓名，日期和评价");
				String input2 = in.nextLine();
				String[] words2 = input2.split(" ");
				input = "insert "
						+ "into checks "
						+ "values('" + words2[0] + "','" + words2[1] + "','" + words2[2] + "','" + words2[3] + "')";
				choice = in.nextLine();
				break;
			case "3":
				System.out.println("输入学生学号和指导老师编号");
				String input3 = in.nextLine();
				String[] words3 = input3.split(" ");
				input = "update student "
						+ "set mno = '" + words3[1] + "' "
						+ "where student.sno = '" + words3[0] + "';";
				choice = in.nextLine(); 
				break;
			case "4":
				choice = in.nextLine();
				break;
			case "..":
				this.show_menu();
				flag = 1;
				break;
			default:
				System.out.println("无法识别的输入！");
				choice = in.nextLine();
				break;
			}
		}
	}
	
	public void show_main_menu(){
//		System.out.println("******大学生住宿管理系统******");
		System.out.println("1.查询");
		System.out.println("2.删除");
		System.out.println("3.添加");
		System.out.println("0.退出");
	}
	
	public static void main(String args[]){
		Lab3 lab3 = new Lab3();
		lab3.show_menu();
	}
}
