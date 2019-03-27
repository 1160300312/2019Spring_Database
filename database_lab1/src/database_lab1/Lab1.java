package database_lab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class Lab1 {
	Connection con = null;
	
	 String firstName="赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯咎管卢莫经房裘缪干解应宗宣丁贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄魏加封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘姜詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲台从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍却璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庚终暨居衡步都耿满弘匡国文寇广禄阙东殴殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后江红游竺权逯盖益桓公万俟司马上官欧阳夏侯诸葛闻人东方赫连皇甫尉迟公羊澹台公冶宗政濮阳淳于仲孙太叔申屠公孙乐正轩辕令狐钟离闾丘长孙慕容鲜于宇文司徒司空亓官司寇仉督子车颛孙端木巫马公西漆雕乐正壤驷公良拓拔夹谷宰父谷粱晋楚阎法汝鄢涂钦段干百里东郭南门呼延归海羊舌微生岳帅缑亢况后有琴梁丘左丘东门西门商牟佘佴伯赏南宫墨哈谯笪年爱阳佟第五言福百家姓续";  
     String girl="秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽 ";  
     String boy="伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";  
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
//		lab1.addEmployee("张三", "410882199801010001", "HeNan", 2500, "41088219980101010x", 3);
//		lab1.addEmployees();
//		lab1.addDepartments();
//		lab1.addProjects();
		lab1.addWorkons();
	}
}
