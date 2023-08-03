package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

public class AddNew extends HttpServlet {
	
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException 
    {
    	System.out.println("called the Add new customer page");
        String fname = req.getParameter("firstName");
        String lname = req.getParameter("lastName");
        String street = req.getParameter("street");
        String address = req.getParameter("address");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
        String queryParam1 = "cmd=create";
        
        HttpSession session = req.getSession();
        String token = session.getAttribute("token").toString();
        String accessToken = token;

        apiUrl = apiUrl + "?" + queryParam1;

        // Prepare the request body as a JSON object
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first_name", fname);
        jsonObject.put("last_name", lname);
        jsonObject.put("street", street);
        jsonObject.put("address", address);
        jsonObject.put("city", city);
        jsonObject.put("state", state);
        jsonObject.put("email", email);
        jsonObject.put("phone", phone);

        	
        // Convert JSON object to JSON string
        String requestBody = jsonObject.toString();

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response from the Create: " + response.toString());
            if(!response.isEmpty())
            {
            	System.out.println("adding object to the session => ");
//            	res.sendRedirect("http://localhost:8080/sample/GetTable")
            	req.getRequestDispatcher("GetTable").forward(req, res);
            	
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred while making the API request.");
        } finally {
            connection.disconnect();
        }
    	 
    	
    	
    }
}