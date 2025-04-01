package übungen;

import java.util.Scanner;

public class money {
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		Double money = scanner.nextDouble();
			
		converttodollar(money);
		converttoyen(money);
		
		scanner.close();
	}

	
	public static void converttodollar(double euro)	{
		System.out.println(euro * 1.20);
	}
	public static void converttoyen(double euro)	{
		System.out.println(euro * 132.45);
	}
}
