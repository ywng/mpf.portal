package portal;

import java.io.*; 


import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import org.json.*;

@WebServlet("/funds")
public class funds  extends HttpServlet{



	@Override
	  public void doGet(HttpServletRequest request,
	                    HttpServletResponse response)
	      throws ServletException, IOException {
	    PrintWriter out = response.getWriter();
	    try {
	    	  MySqlAccess.run();
	    	  String fundName[]=MySqlAccess.allFund;
	    	  JSONArray list = new JSONArray();
	    	  for(int i=0;i<fundName.length;i++){
	    		  list.put(fundName[i]);
	    	  }
	    	  StringWriter jsonOut = new StringWriter();
	    	  list.write(jsonOut);
	    	  String jsonText = jsonOut.toString();
	    	  out.print(jsonText);
	    	  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	  }
}
