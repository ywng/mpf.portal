package portal;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ExtracterWeb {
	private static PrintWriter out;
	
	public static void run(PrintWriter _out) throws Exception{
		out=_out;
		MySqlAccess.run();
	//	importBatch();
		dailyExtract();
	}
	
	public static void dailyExtract() throws SQLException{

		out.println("\nExtracting unit price:");
		UnitPrice APEF=extractPrice("http://www.bloomberg.com/quote/HBSTPAE:HK");
		MySqlAccess.updateUnitPrice(1, APEF);
		UnitPrice BF=extractPrice("http://www.bloomberg.com/quote/HBSTPBA:HK");
		MySqlAccess.updateUnitPrice(2, BF);
		UnitPrice CEF=extractPrice("http://www.bloomberg.com/quote/HSTCHEQ:HK");
		MySqlAccess.updateUnitPrice(3, CEF);
		UnitPrice EEF=extractPrice("http://www.bloomberg.com/quote/HBSTPEU:HK");
		MySqlAccess.updateUnitPrice(4, EEF);
		UnitPrice FMF=extractPrice("http://www.bloomberg.com/quote/HSTFXMG:HK");
		MySqlAccess.updateUnitPrice(5, FMF);
		UnitPrice GBF=extractPrice("http://www.bloomberg.com/quote/HSTGLBD:HK");
		MySqlAccess.updateUnitPrice(6, GBF);
		UnitPrice GF=extractPrice("http://www.bloomberg.com/quote/HBSTPGR:HK");
		MySqlAccess.updateUnitPrice(7, GF);
		UnitPrice GuaranF=extractPrice("http://www.bloomberg.com/quote/HBSTPGU:HK");
		MySqlAccess.updateUnitPrice(8, GuaranF);
		UnitPrice HSITF=extractPrice("http://www.bloomberg.com/quote/HBSTPHS:HK");
		MySqlAccess.updateUnitPrice(9, HSITF);
		UnitPrice HKCEF=extractPrice("http://www.bloomberg.com/quote/HBSTPHK:HK");
		MySqlAccess.updateUnitPrice(10, HKCEF);
		UnitPrice MPFCF=extractPrice("http://www.bloomberg.com/quote/HBSTPCP:HK");
		MySqlAccess.updateUnitPrice(11, MPFCF);
		UnitPrice NAEF=extractPrice("http://www.bloomberg.com/quote/HBSTPNA:HK/chart");
		MySqlAccess.updateUnitPrice(12, NAEF);
		UnitPrice SF=extractPrice("http://www.bloomberg.com/quote/HSTSTAB:HK");
		MySqlAccess.updateUnitPrice(13, SF);
		UnitPrice SGF=extractPrice("http://www.bloomberg.com/quote/HBSTPSG:HK");
		MySqlAccess.updateUnitPrice(14, SGF);
		
	}
	
	public static void importBatch() throws SQLException{
		MySqlAccess.updateFileBatch("Asia Pacific Equity Fund.csv");
		MySqlAccess.updateFileBatch("Balanced Fund.csv");
		MySqlAccess.updateFileBatch("Chinese Equity Fund.csv");
		MySqlAccess.updateFileBatch("European Equity Fund.csv");
		MySqlAccess.updateFileBatch("Flexi-Managed Fund.csv");
		MySqlAccess.updateFileBatch("Global Bond Fund.csv");
		MySqlAccess.updateFileBatch("Growth Fund.csv");
		MySqlAccess.updateFileBatch("Guaranteed Fund.csv");
		MySqlAccess.updateFileBatch("Hang Seng Index Tracking Fund.csv");
		MySqlAccess.updateFileBatch("Hong Kong and Chinese Equity Fund.csv");
		MySqlAccess.updateFileBatch("MPF Conservative Fund.csv");
		MySqlAccess.updateFileBatch("North American Equity Fund.csv");
		MySqlAccess.updateFileBatch("Stable Fund.csv");
		MySqlAccess.updateFileBatch("Stable Growth Fund.csv");
	}
	
	public static UnitPrice extractPrice(String url){
		Document doc;
		UnitPrice priceObj=new UnitPrice();
		boolean noError=false;
		while(!noError){
			try {
				 
				// need http protocol
				doc = Jsoup.connect(url).get();
		 
				// get page title
				String title = doc.title();
				String temp[]=title.split("-");
				out.println("title:" + temp[3]);
		 
				// get all links
				Elements elements = doc.select("span.price");
				for (Element e : elements) {
				     // System.out.println("NAV:" + e.ownText());
				      priceObj.price=Float.parseFloat(e.ownText());
				}
				
				Elements date = doc.select("p[class=fine_print]");
				for (Element e : date) {
				     // System.out.println( e.ownText());
				      String dateRaw=e.ownText().substring(e.ownText().length()-11,e.ownText().length()-1);
				      //System.out.println( dateRaw);
				      temp=dateRaw.split("/");
				      priceObj.year=Integer.parseInt(temp[2]);
				      priceObj.month=Integer.parseInt(temp[0]);
				      priceObj.day=Integer.parseInt(temp[1]);
				}
				
				noError=true;
			} catch (IOException e) {
				e.printStackTrace();
				noError=false;
			}
		}
		
		priceObj.print(out);
		return priceObj;
	}

}
