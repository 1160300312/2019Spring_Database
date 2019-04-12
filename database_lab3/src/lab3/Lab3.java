package lab3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
			System.out.println("6.通过姓名查询某个学生的所有亲属及联系方式");
			System.out.println("7.查询所有员工的情况");
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
			System.out.println("5.插入一条员工记录");
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
			input = "";
			switch(choice){
			case "1":
				System.out.println("输入要查询学生的姓名：");
				String input1 = in.nextLine();
				input = "select * "
						+ "from student "
						+ "where name = '" + input1 + "';";
				break;
			case "2":
				System.out.println("输入要查询的教师的姓名：");
				String input2 = in.nextLine();
				input = "select sm.sname "
						+ "from student_mentor sm "
						+ "where sm.mname =  '" + input2 + "';";
				break;
			case "3":
				System.out.println("输入要查询的公寓的名字：");
				String input3 = in.nextLine();
				input = "select check_date,evaluation "
						+ "from checks "
						+ "where checks.dorm_name= '" + input3 + "';";
				break;
			case "4":
				input = "select student.sno,count(*) "
						+ "from student,elective_list "
						+ "where student.sno = elective_list.sno "
						+ "group by student.sno";
				break;
			case "5":
				System.out.println("输入课程的号：");
				String input5 = in.nextLine();
				input = "select student.name "
						+ "from student "
						+ "where sno not in ("
						+ "select student.sno "
						+ "from student,elective_list "
						+ "where student.sno=elective_list.sno and elective_list.cno = '" + input5 + "');";
				break;
			case "6":
				System.out.println("输入学生姓名：");
				String input6 = in.nextLine();
				input = "select fam.relation,fam.number "
						+ "from student_family fam "
						+ "where fam.sname = '" + input6 + "';";
				break;
			case "7":
				input = "select* "
						+ "from manager";
				break;
			case "..":
				this.show_menu();
				flag = 1;
				break;
			default:
				System.out.println("无法识别的输入！");
				break;
			}
//			System.out.println(input);
			if(!input.equals("")){
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
			if(!choice.equals("..")){
				choice = in.nextLine();
			}
		}
	}
	
	public void delete(String choice){
		Scanner in = new Scanner(System.in);
		String input = "";
		int flag = 0;
		while(flag == 0){
			input = "";
			switch(choice){
			case "1":
				System.out.println("输入要删除学生的学号：");
				String input1 = in.nextLine();
				input = "delete "
						+ "from student "
						+ "where student.sno = '" + input1 + "';";
				break;
			case "2":
				System.out.println("输入要删除的学生学号和课程号：");
				String input2 = in.nextLine();
				String[] words2 = input.split(" ");
				input = "delete "
						+ "from elective_list "
						+ "where sno = '" + words2[0] + "' and cno = '" + words2[1] + "';";
				break;
			case "3":
				System.out.println("输入日期和公寓名：");
				String input3 = in.nextLine();
				String[] words3 = input3.split(" ");
				input = "delete "
						+ "from checks "
						+ "where check_date = '" + words3[0] + "' and dorm_name = '" + words3[1] + "';";
				break;
			case "..":
				this.show_menu();
				flag = 1;
				break;
			default:
				System.out.println("无法识别的输入！");
				break;
			}
			if(!input.equals("")){
//				System.out.println(input);
				try {
					PreparedStatement ps = this.con.prepareStatement(input);
					ps.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
//					System.out.println(e.getMessage());
					if(e.getMessage().matches(".*Cannot delete or update a parent row.*")){
						System.out.println("删除失败，存在外键依赖与该记录的主键");
					}
				}

			}
			if(!choice.equals("..")){
				choice = in.nextLine();
			}
		}
	}
	
	public void insert(String choice){
		Scanner in = new Scanner(System.in);
		String input = "";
		int flag = 0;
		while(flag == 0){
			input = "";
			switch(choice){
			case "1":
				System.out.println("输入学生课程号和学号：");
				String input1 = in.nextLine();
				String[] words1 = input1.split(" ");
				input = "insert "
						+ "into elective_list "
						+ "values('" + words1[0] + "','" + words1[1] + "');";
				if(words1[0].equals("null")||words1[1].equals("null")){
					input = "insert "
							+ "into elective_list "
							+ "values(" + "null" + ",'" + words1[1] + "');";
				}
				break;
			case "2":
				System.out.println("请依次输入检查员编号，公寓名，日期和评价");
				String input2 = in.nextLine();
				String[] words2 = input2.split(" ");
				input = "insert "
						+ "into checks "
						+ "values('" + words2[0] + "','" + words2[1] + "','" + words2[2] + "','" + words2[3] + "');";
				if(words2[0].equals("null")||words2[1].equals("null")){
					input = "insert "
							+ "into checks "
							+ "values(" + "null"+ ",'" + words2[1] + "','" + words2[2] + "','" + words2[3] + "');";
				}
				break;
			case "3":
				System.out.println("输入学生学号和指导老师编号");
				String input3 = in.nextLine();
				String[] words3 = input3.split(" ");
				input = "update student "
						+ "set mno = '" + words3[1] + "' "
						+ "where student.sno = '" + words3[0] + "';";
				break;
			case "4":
				System.out.println("请依次输入学生学号，姓名，家庭住址，出生日期和指导教师编号");
				String input4 = in.nextLine();
				String[] words4 = input4.split(" ");
				input = "insert "
						+ "into student "
						+ "values('" + words4[0] + "','" + words4[1] + "','" + words4[2] + "','" + words4[3] + "','" + words4[4] + "');";
				if(words4[0].equals("null")){
					input = "insert "
							+ "into student "
							+ "values(" + words4[0] + ",'" + words4[1] + "','" + words4[2] + "','" + words4[3] + "','" + words4[4] + "');";
				}
				break;
			case "5":
				System.out.println("请一次输入员工职位，员工号，姓名，电话和薪水");
				String input5 = in.nextLine();
				String[] words5 = input5.split(" ");
				input = "insert "
						+ "into manager "
						+ "values('" + words5[0] + "','" + words5[1] + "','" + words5[2] + "','" + words5[3] + "'," + words5[4] + ");";
				break;
			case "..":
				this.show_menu();
				flag = 1;
				break;
			default:
				System.out.println("无法识别的输入！");
				break;
			}
			if(!input.equals("")){
				try {
//					System.out.println(input);  //////
					PreparedStatement ps = this.con.prepareStatement(input);
					ps.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
//					System.out.println(e.getMessage());
					if(e.getMessage().matches(".*cannot be null.*")){
						System.out.println("插入失败，主键不能为空");
					}
					if(e.getMessage().matches(".*Duplicate entry.*")){
						System.out.println("插入失败，主键存在冲突值");
					}
					if(e.getMessage().matches(".*a foreign key constraint fails.*")){
						System.out.println("插入失败，外键对应的主键不存在");
					}
				}
			}
			if(!choice.equals("..")){
				choice = in.nextLine();
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
