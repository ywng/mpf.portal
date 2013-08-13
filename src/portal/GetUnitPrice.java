package portal;

import java.io.*; 

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/GetUnitPrice")
public class GetUnitPrice  extends HttpServlet{

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	  public void doGet(HttpServletRequest request,
	                    HttpServletResponse response)
	      throws ServletException, IOException {
	    PrintWriter out = response.getWriter();
	    try {
			ExtracterWeb.run(out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	  }
}
