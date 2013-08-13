package portal;

import java.io.*; 


import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import org.json.*;

@WebServlet("/AllUnitPrice")
public class AllUnitPrice  extends HttpServlet{



	@Override
	  public void doGet(HttpServletRequest request,
	                    HttpServletResponse response)
	      throws ServletException, IOException {
	    PrintWriter out = response.getWriter();
	    try {
	    	  MySqlAccess.run();
	    	  response.setContentType("application/json");
	    	  out.print(MySqlAccess.getAllPrice());
	    	  out.flush();
	    	  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	  }
}
