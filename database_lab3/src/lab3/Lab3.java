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
			System.out.println("1.ͨ��ѧ��������ѯѧ����Ϣ");
			System.out.println("2.ͨ����ʦ����������ָ����ѧ��");
			System.out.println("3.��ѯĳ��Ԣ����������");
			System.out.println("4.��ѯÿ��ѧ��ѡ�޵Ŀγ̵�����");
			System.out.println("5.��ѯû��ѡ��ĳ�ſε�ѧ������");
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
			switch(choice){
			case "1":
				System.out.print("����Ҫ��ѯѧ����������");
				String input1 = in.nextLine();
				input = "select * "
						+ "from student "
						+ "where name = '" + input + "';";
				choice = in.nextLine();
				break;
			case "2":
				System.out.print("����Ҫ��ѯ�Ľ�ʦ��������");
				String input2 = in.nextLine();
				input = "select student.name "
						+ "from student,mentor "
						+ "where student.mno = mentor.mno and mentor.name = '" + input + "';";
				choice = in.nextLine();
				break;
			case "3":
				System.out.print("����Ҫ��ѯ�Ĺ�Ԣ�����֣�");
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
				System.out.println("����γ̵ĺţ�");
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
				System.out.println("�޷�ʶ������룡");
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
				System.out.print("����Ҫɾ��ѧ����ѧ�ţ�");
				String input1 = in.nextLine();
				input = "delete "
						+ "from student "
						+ "where student.sno = '" + input + "';";
				choice = in.nextLine();
				break;
			case "2":
				System.out.print("����Ҫɾ����ѧ��ѧ�źͿγ̺ţ�");
				String input2 = in.nextLine();
				String[] words2 = input.split(" ");
				input = "delete "
						+ "from elective_list "
						+ "where sno = '" + words2[0] + "' and cno = '" + words2[1] + "';";
				choice = in.nextLine();
				break;
			case "3":
				System.out.print("�������ں͹�Ԣ����");
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
				System.out.println("�޷�ʶ������룡");
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
				System.out.print("����ѧ��ѧ�źͿγ̺ţ�");
				String input1 = in.nextLine();
				String[] words1 = input1.split(" ");
				input = "insert "
						+ "into elective_list "
						+ "values('" + words1[0] + "','" + words1[1] + "');";
				choice = in.nextLine();
				break;
			case "2":
				System.out.println("������������Ա��ţ���Ԣ�������ں�����");
				String input2 = in.nextLine();
				String[] words2 = input2.split(" ");
				input = "insert "
						+ "into checks "
						+ "values('" + words2[0] + "','" + words2[1] + "','" + words2[2] + "','" + words2[3] + "')";
				choice = in.nextLine();
				break;
			case "3":
				System.out.println("����ѧ��ѧ�ź�ָ����ʦ���");
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
				System.out.println("�޷�ʶ������룡");
				choice = in.nextLine();
				break;
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
