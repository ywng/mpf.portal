package portal;

import java.io.PrintWriter;

public class UnitPrice {
	public float price;
	public int year;
	public int month;
	public int day;
	
	public void print(PrintWriter out){
		out.println("Price: "+price+" as of "+year+"-"+month+"-"+day);
	}

}
