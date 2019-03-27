package database_lab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class Lab1 {
	Connection con = null;
	
	 String firstName="��Ǯ��������֣��������������������������ʩ�ſײ��ϻ���κ�ս���л������ˮ��������˸��ɷ�����³Τ������ﻨ������Ԭ��ۺ��ʷ�Ʒ����Ѧ�׺����������ޱϺ�����������ʱ��Ƥ���뿵����Ԫ������ƽ�ƺ�������Ҧ��տ����ë����ױ���갼Ʒ��ɴ�̸��é���ܼ�������ף������������ϯ����ǿ��·¦Σ��ͯ�չ�÷ʢ�ֵ�����������Ĳ��﷮���������֧�¾̹�¬Ī�������Ѹɽ�Ӧ�������ڵ��������������ʯ�޼�ť�������ϻ���½��������춻���κ�ӷ����ഢ���������ɾ��θ����ڽ��͹�����ɽ�ȳ������ȫۭ�����������������ﱩ�����������������ղ����Ҷ��˾��۬�輻��ӡ�ް׻���̨�Ӷ����̼���׿�����ɳ����������ܲ�˫��ݷ����̷�����̼������Ƚ��۪Ӻȴ�ɣ���ţ��ͨ�����༽ۣ����ũ�±�ׯ�̲����ֳ�Ľ����ϰ�°���������������θ����߾Ӻⲽ�����������Ŀܹ�»�ڶ�Ź�����εԽ��¡ʦ�������˹��������������Ǽ��Ŀ�����ɳؿ������ᳲ�������󽭺�����Ȩ�ָ��滸����ٹ˾���Ϲ�ŷ���ĺ�������˶��������ʸ�ξ�ٹ����̨��ұ���������������̫����������������ԯ�������������Ľ����������˾ͽ˾������˾���붽�ӳ�����ľ����������������ṫ���ذμй��׸����������ַ���۳Ϳ�նθɰ��ﶫ�����ź��ӹ麣����΢����˧�ÿ�������������������������Ĳ��٦�����Ϲ�ī�������갮��١�����Ը��ټ�����";  
     String girl="���Ӣ���������Ⱦ���������֥��Ƽ�����ҷ���ʴ��������÷���������滷ѩ�ٰ���ϼ����ݺ�����𷲼Ѽ�������������Ҷ�������������ɺɯ������ٻ�������ӱ¶������������Ǻɵ���ü������ޱݼ���Է�ܰ�������԰��ӽ�������ع���ѱ�ˬ������ϣ����Ʈ�����������������������ܿ�ƺ������˿ɼ���Ӱ��֦˼�� ";  
     String boy="ΰ�����㿡��ǿ��ƽ�����Ļ�������������־��������ɽ�ʲ���������Ԫȫ��ʤѧ��ŷ���������ɱ�˳���ӽ��β��ɿ��ǹ���ﰲ����ï�����м�ͱ벩���Ⱦ�����׳��˼Ⱥ���İ�����ܹ����ƺ���������ԣ���ܽ���������ǫ�����֮�ֺ��ʲ����������������ά�������������󳿳�ʿ�Խ��������׵���ʱ̩ʢ��衾��ڲ�����ŷ纽��";  
     String[] department = {"Personnel", "Sales", "Inter", "Export", "Inport", "HR", "Marketing", "Engineer", "Admin", "Financial"};
   
     
	public Lab1(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/COMPANY?useSSL=false&serverTimezone = GMT","root","123456");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void addEmployee(String name,String essn, String address, int salary, String superssn,int dno){
		String sql = "insert into employee values('" + name + "','" + essn + "','" + address + "'," + salary + ",'"
				 + superssn + "'," + dno + ")";
		try {
			PreparedStatement ps = this.con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addDepartment(String dname,int dno, String mgrssn, String date){
		String sql = "insert into department values('" + dname + "'," + dno + ",'" + mgrssn + "','" + date + "')";
		try {
			PreparedStatement ps = this.con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addProject(String pname, String pno, String location, int dno){
		String sql = "insert into project values('" + pname + "','" + pno + "','" + location + "'," + dno + ")";
		try {
			PreparedStatement ps = this.con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addWorkon(String essn, String pno, int hours){
		String sql = "insert into works_on values('" + essn + "','" + pno + "'," + hours + ")";
		try {
			PreparedStatement ps = this.con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addEmployees(){
		//add leader
		Random r = new Random();
		for(int i=11;i<=20;i++){
			int rand1 = r.nextInt(this.firstName.length());
			String firstname = this.firstName.substring(rand1, rand1+1);
			int rand2 = r.nextInt(this.boy.length()-1);
			String name = this.boy.substring(rand2, rand2+2);
			String essn = "4108801998010101" + i;
			String address = "henan";
			String superssn = "410882199805081111";
			int dno = 100;
			int salary = r.nextInt(1000)+2501;
			this.addEmployee(firstname+name, essn, address, salary, superssn, dno);
		}
		
		for(int i=100;i<200;i++){
			int rand1 = r.nextInt(this.firstName.length());
			String firstname = this.firstName.substring(rand1, rand1+1);
			int rand2 = r.nextInt(this.boy.length()-1);
			String name = this.boy.substring(rand2, rand2+2);
			String essn = "410882199801011" + i;
			String address = "henan";
			String superssn = "4108821998010101" + ((i%11) + 10);
			int dno = (i%11) + 10;
			int salary = r.nextInt(1000)+2501;
			this.addEmployee(firstname+name, essn, address, salary, superssn, dno);
		}
		
	}
	
	public void addDepartments(){
		for(int i=11;i<=20;i++){
			String dname = this.department[i-11] + " Department";
			int dno = i;
			String mgrssn = "4108821998010101"+i;
			String date = "2018-01-" + i;
			this.addDepartment(dname, dno, mgrssn, date);
		}
	}
	
	public void addProjects(){
		for(int i=1;i<=20;i++){
			String pno = "P" + i;
			String pname = "project " + i;
			String plocation = "henan";
			int dno = i%11 + 10;
			this.addProject(pname, pno, plocation, dno);
		}
	}
	
	public void addWorkons(){
		Random r = new Random();
		/*for(int i=100;i<=199;i++){
			for(int j=0;j<i%4+1;j++){
				int proj = r.nextInt(20) + 1;
				int time = r.nextInt(5) + 3;
				String pno = "P" + proj;
				String essn = "410882199801011" + i;
				this.addWorkon(essn, pno, time);
			}
		} */
		
		/*for(int i=0;i<5;i++){
			for(int j=1;j<=20;j++){
				String pno = "P" + j;
				String essn = "41088219980101115" + i;
				int hours = r.nextInt(3) + 2;
				this.addWorkon(essn, pno, hours);
			}
		}*/
		
		
	}
	
	public static void main(String args[]){
		Lab1 lab1 = new Lab1();
//		lab1.addEmployee("����", "410882199801010001", "HeNan", 2500, "41088219980101010x", 3);
//		lab1.addEmployees();
//		lab1.addDepartments();
//		lab1.addProjects();
		lab1.addWorkons();
	}
}
