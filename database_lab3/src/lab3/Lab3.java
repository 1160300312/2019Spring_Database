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
			System.out.println("1.ͨ��ѧ��������ѯѧ����Ϣ");
			System.out.println("2.ͨ����ʦ����������ָ����ѧ��");
			System.out.println("3.��ѯĳ��Ԣ����������");
			System.out.println("4.��ѯÿ��ѧ��ѡ�޵Ŀγ̵�����");
			System.out.println("5.��ѯû��ѡ��ĳ�ſε�ѧ������");
			System.out.println("6.ͨ��������ѯĳ��ѧ����������������ϵ��ʽ");
			System.out.println("7.��ѯ����Ա�������");
			String choice2 = s.nextLine();
			this.inquire(choice2);
			break;
		case 2:
			System.out.println("1.ͨ��ѧ��ɾ��ѧ��");
			System.out.println("2.ɾ��ĳѧ����ĳ��ѡ�μ�¼");
			System.out.println("3.ɾ��ĳ���ĳ��Ԣ�ļ��");
			String choice3 = s.nextLine();
			this.delete(choice3);
			break;
		case 3:
			System.out.println("1.���ĳѧ��ѡ��ĳ�γ�");
			System.out.println("2.���һ�ι�Ԣ����¼");
			System.out.println("3.Ϊĳѧ������ָ����ʦ");
			System.out.println("4.���ѧ��");
			System.out.println("5.����һ��Ա����¼");
			String choice4 = s.nextLine();
			this.insert(choice4);
			break;
		case 0:
			break;
		default:
			System.out.println("�޷�ʶ������룡");
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
				System.out.println("����Ҫ��ѯѧ����������");
				String input1 = in.nextLine();
				input = "select * "
						+ "from student "
						+ "where name = '" + input1 + "';";
				break;
			case "2":
				System.out.println("����Ҫ��ѯ�Ľ�ʦ��������");
				String input2 = in.nextLine();
				input = "select sm.sname "
						+ "from student_mentor sm "
						+ "where sm.mname =  '" + input2 + "';";
				break;
			case "3":
				System.out.println("����Ҫ��ѯ�Ĺ�Ԣ�����֣�");
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
				System.out.println("����γ̵ĺţ�");
				String input5 = in.nextLine();
				input = "select student.name "
						+ "from student "
						+ "where sno not in ("
						+ "select student.sno "
						+ "from student,elective_list "
						+ "where student.sno=elective_list.sno and elective_list.cno = '" + input5 + "');";
				break;
			case "6":
				System.out.println("����ѧ��������");
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
				System.out.println("�޷�ʶ������룡");
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
				System.out.println("����Ҫɾ��ѧ����ѧ�ţ�");
				String input1 = in.nextLine();
				input = "delete "
						+ "from student "
						+ "where student.sno = '" + input1 + "';";
				break;
			case "2":
				System.out.println("����Ҫɾ����ѧ��ѧ�źͿγ̺ţ�");
				String input2 = in.nextLine();
				String[] words2 = input.split(" ");
				input = "delete "
						+ "from elective_list "
						+ "where sno = '" + words2[0] + "' and cno = '" + words2[1] + "';";
				break;
			case "3":
				System.out.println("�������ں͹�Ԣ����");
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
				System.out.println("�޷�ʶ������룡");
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
						System.out.println("ɾ��ʧ�ܣ��������������ü�¼������");
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
				System.out.println("����ѧ���γ̺ź�ѧ�ţ�");
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
				System.out.println("������������Ա��ţ���Ԣ�������ں�����");
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
				System.out.println("����ѧ��ѧ�ź�ָ����ʦ���");
				String input3 = in.nextLine();
				String[] words3 = input3.split(" ");
				input = "update student "
						+ "set mno = '" + words3[1] + "' "
						+ "where student.sno = '" + words3[0] + "';";
				break;
			case "4":
				System.out.println("����������ѧ��ѧ�ţ���������ͥסַ���������ں�ָ����ʦ���");
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
				System.out.println("��һ������Ա��ְλ��Ա���ţ��������绰��нˮ");
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
				System.out.println("�޷�ʶ������룡");
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
						System.out.println("����ʧ�ܣ���������Ϊ��");
					}
					if(e.getMessage().matches(".*Duplicate entry.*")){
						System.out.println("����ʧ�ܣ��������ڳ�ͻֵ");
					}
					if(e.getMessage().matches(".*a foreign key constraint fails.*")){
						System.out.println("����ʧ�ܣ������Ӧ������������");
					}
				}
			}
			if(!choice.equals("..")){
				choice = in.nextLine();
			}
		}
	}
	
	public void show_main_menu(){
//		System.out.println("******��ѧ��ס�޹���ϵͳ******");
		System.out.println("1.��ѯ");
		System.out.println("2.ɾ��");
		System.out.println("3.���");
		System.out.println("0.�˳�");
	}
	
	public static void main(String args[]){
		Lab3 lab3 = new Lab3();
		lab3.show_menu();
	}
}
