package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class GetTable extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
     

        HttpSession session=req.getSession();
        String token=session.getAttribute("token").toString();
        
        // Send the GET request to the specified URL
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoOutput(true);
        

        // Read the response from the connection
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response from the GET: " + response.toString());
            
//            JSONObject jsonObject = new JSONObject(response.toString());

           if(!response.isEmpty())
           {
        	  String StringResponse=response.toString();
        	  Gson gson = new Gson();
              Customer[] customersArray = gson.fromJson(StringResponse, Customer[].class);

              // Convert the array to a list if needed
              List<Customer> customersList = Arrays.asList(customersArray);
              System.out.println("array list created is "+customersList);
        	  
//        	  req.setAttribute("customerList",customersList);
        	  session.setAttribute("customersList", customersList);
        	  req.getRequestDispatcher("Table.jsp").forward(req, res);
    
           }
            //string in cookie storage
          
            // Write the response to the servlet's response
            res.setContentType("text/plain");
            res.getWriter().println("Response from the API GET: " + response.toString());
        } catch (IOException e) {
        	 res.getWriter().println("API request failed, Invalid credentials");
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
}
