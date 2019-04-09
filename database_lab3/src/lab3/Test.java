package lab3;

import java.util.Scanner;

public class Test {
	public static void main(String args[]){
		String input = "Column 'sno' cannot be null";
		if(input.matches(".*cannot be null.*")){
			System.out.println(1);
		}
	}
}
