package portal;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Java program to connect to MySQL Server database running on localhost,
 * using JDBC type 4 driver.
 *
 * @author http://java67.blogspot.com
 */
public class MySqlAccess{
	private static String dbURL;
    private static String username;
    private static String password;
    private static Connection dbCon;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    
    protected static String[] allFund=null;


    public static void run() throws ClassNotFoundException {

    	Class.forName("com.mysql.jdbc.Driver");
    	dbURL = "jdbc:mysql://db4free.net:3306/hangsengmpf";
    	username ="xxxxx";
    	password = "xxxxxx";
       
        dbCon = null;
        //getting database connection to MySQL server
        try {
			dbCon = DriverManager.getConnection(dbURL, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        allFund=getFundNameList();

    }  
    
    public static  String getAllPrice() throws JSONException{
    	String jsonStr = null;
    	Statement stmt = null;
        ResultSet rs = null;

        try {

        	String query ="SELECT * FROM UnitPrice ORDER BY ID ASC, Year ASC, Month ASC, Day ASC";
            stmt = dbCon.prepareStatement(query);
            rs = stmt.executeQuery(query);
            
            JSONArray ALL = new  JSONArray();
            
            int ID=1;
            JSONArray tempList = new JSONArray();
            JSONArray dataList = new JSONArray();
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            JSONObject objFundName = new JSONObject();
            objFundName.put("name",allFund[ID-1]);
            tempList.put( objFundName);
     
            while(rs.next()){
            
              if(rs.getInt(1)!=ID){
            	  JSONObject objData = new JSONObject();
                  objData.put("data",dataList);
            	  tempList.put(objData);
            	  ALL.put(tempList);
            	  ID=rs.getInt(1);
            	  tempList=new JSONArray();
            	  dataList = new JSONArray();
            	  objFundName   = new JSONObject();
            	  objFundName.put("name",allFund[ID-1]);
            	  tempList.put( objFundName); 
              }
              
              JSONObject obj = new JSONObject();
              obj.put("price",df.format(rs.getFloat(2)));
              obj.put("year",rs.getInt(3));
              obj.put("month",rs.getInt(4));
              obj.put("day",rs.getInt(5));
              
              dataList.put(obj);

            }
            //ending one 
            JSONObject objData = new JSONObject();
            objData.put("data",dataList);
            tempList.put(objData);
            ALL.put(tempList);
      	  
            StringWriter jsonOut = new StringWriter();
	    	ALL.write(jsonOut);
	    	jsonStr = jsonOut.toString();
            
        } catch (SQLException ex) {
           
        } finally{
           //close connection ,stmt and resultset here
        }
        return jsonStr;
    }
    

    
    public static  String[] getFundNameList(){
    	Statement stmt = null;
        ResultSet rs = null;
        String[] fundList;
        try {

        	String query ="select count(*) from MPF_Funds";
            stmt = dbCon.prepareStatement(query);
            rs = stmt.executeQuery(query);
            rs.next();
           
            fundList=new String[rs.getInt(1)];
            
            query ="select * from MPF_Funds";
            stmt = dbCon.prepareStatement(query);
            rs = stmt.executeQuery(query);
            int counter=0;
            while(rs.next()){
            	
            	fundList[counter]=rs.getString(2);
            	counter++;
            }
            return fundList;
        } catch (SQLException ex) {
           
        } finally{
           //close connection ,stmt and resultset here
        }
        return null;
    }
   
    public static int updateUnitPrice(int ID, UnitPrice priceObj) throws SQLException{
    	Statement stmt = null;
        ResultSet rs = null;
        
    	String query ="SELECT count(*) FROM `UnitPrice` WHERE `ID` ="+ID+" AND `Year` ="+priceObj.year+" AND `Month` ="+priceObj.month+" AND `Day` ="+priceObj.day;
    	
    	stmt = dbCon.prepareStatement(query);
        rs = stmt.executeQuery(query);
        rs.next();
        if(rs.getInt(1)!=0){
        	
        	return 0;
        }else{
        	stmt = dbCon.createStatement(); 
            stmt.executeUpdate("INSERT INTO `UnitPrice` " + 
                "VALUES ("+ID+","+priceObj.price+","+priceObj.year+","+priceObj.month+","+priceObj.day+")"); 
            stmt=null;
        	return 1;
        }
    }
    
    public static int updateFileBatch(String fileName) throws SQLException{
    	int count=0;
    	
    	//read file
    	BufferedReader br = null;
    	String line = "";
    	String cvsSplitBy = ",";
     
    	try {
     
    		br = new BufferedReader(new FileReader(fileName));
    		line = br.readLine();
    		String[] temp = line.split(cvsSplitBy);
    		
    		int ID=searchFundID(temp[0]);
    		
    		while ((line = br.readLine()) != null) {
     
    		        // use comma as separator
    			temp = line.split(cvsSplitBy);
    			String date[]=temp[0].split("-");
    			UnitPrice record=new UnitPrice();
    			record.price=Float.parseFloat(temp[1]);
    			record.day=Integer.parseInt(date[2]);
    			record.month=Integer.parseInt(date[1]);
    			record.year=Integer.parseInt(date[0]);
    			
    			count+=updateUnitPrice(ID,record);
    			
    			
     
    		
     
    		}
     
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if (br != null) {
    			try {
    				br.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	
    	
    	
    	return count;
    }
    
    public static int searchFundID(String fundName){
    	for(int i=0;i<allFund.length;i++){
    		if(allFund[i].equalsIgnoreCase(fundName)){
    			return i+1;
    		}
    	}
    		
    		
    	return -1;
    	
    }
    
    public static void cleanErrorRecord() throws SQLException{
    	String query ="DELETE FROM UnitPrice WHERE Year=0 and Month=0 and Day=0 ";
    	stmt = dbCon.createStatement();
    	stmt.executeUpdate(query);
    }
}
