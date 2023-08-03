package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/DeleteCustomer")
public class DeleteCustomer extends HttpServlet {
	
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
    	
    	System.out.println("called the class");
    	// Call the static method of your Java class here
    	String id = req.getParameter("id");
    	System.out.println(id);
    	String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
    	String queryParam1 = "cmd=delete";
    	String queryParam2="uuid="+id;
    	HttpSession session=req.getSession();
        String token=session.getAttribute("token").toString();
    	String accessToken =token;

    	// Append the query parameter to the API URL
    	apiUrl = apiUrl + "?" + queryParam1+"&"+queryParam2;

    	URL url = new URL(apiUrl);
    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	connection.setRequestMethod("POST");

    	// Add Authorization header to the request
    	connection.setRequestProperty("Authorization", "Bearer " + accessToken);
    	connection.setRequestProperty("Content-Type", "application/json");
    	connection.setDoOutput(true);
    	
    	// Read the response from the connection
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
          System.out.println("Response from the Delete: " + response.toString());
          if(!response.isEmpty())
          {
        	  List<Customer> ls = (List<Customer>) session.getAttribute("customersList");
        	  if (ls != null) {
        	      // Create a new modifiable list to perform removals
        	      List<Customer> modifiableList = new ArrayList<>(ls);

        	      Iterator<Customer> iterator = modifiableList.iterator();
        	      while (iterator.hasNext()) {
        	          Customer customer = iterator.next();
        	          if (customer.getUuid().equals(id)) {
        	              System.out.print("user removing is =>");
        	              System.out.println(customer.getFirst_name() + " " + customer.getLast_name());
        	              iterator.remove(); // This safely removes the customer from the list
        	          }
        	          System.out.println(customer);
        	      }
        	      System.out.println("After removing the item from the session");
        	      for (Customer customer : modifiableList) {
        	          System.out.println(customer);
        	      }

        	      // Update the session attribute with the modified list
        	      session.setAttribute("customersList", modifiableList);

        	      // Set a header to trigger a page reload
        	      res.setHeader("Refresh", "0");

        	      // You can send a response back to the client if needed
        	      res.getWriter().write("location.reload();");
        	  }



          }
         
    	
    

        // You can send a response back to the client if needed

    }
}
};