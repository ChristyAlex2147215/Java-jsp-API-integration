//package sample;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class Login extends HttpServlet {
//	
//	public void service(HttpServletRequest req,HttpServletResponse res) throws IOException
//	{
//		String email=req.getParameter("email");
//		String password=req.getParameter("password");
//		System.out.println(email);
//		System.out.println(password);
//		
//		PrintWriter out=res.getWriter();
//		out.println("result is "+email);
//	}
//
//}

package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login extends HttpServlet {
	private String accessToken; 

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        System.out.println(email);
        System.out.println(password);

        // Prepare the request body
        String requestBody = "{\n" +
                "\"login_id\" : \"" + email + "\",\n" +
                "\"password\" : \"" + password + "\"\n" +
                "}";

        // Send the GET request to the specified URL
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Write the request body to the connection's output stream
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Read the response from the connection
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Response: " + response.toString());
            
            JSONObject jsonObject = new JSONObject(response.toString());

            // Access the access_token
            String accessToken = jsonObject.getString("access_token");

            // Print the access_token
            System.out.println(accessToken);
            this.accessToken=accessToken;
            //storing in session storage
            HttpSession session=req.getSession();
            session.setAttribute("token", accessToken);
            //string in cookie storage
            if(session.getAttribute("token")!="")
            {
            	System.out.println("TOken saved to sesssion");
            	
            	RequestDispatcher rd=req.getRequestDispatcher("GetTable");
            	rd.forward(req, res);
            	
            }

            // Write the response to the servlet's response
            res.setContentType("text/plain");
            res.getWriter().println("Response from the API: " + response.toString());
        } catch (IOException e) {
        	 res.getWriter().println("API request failed, Invalid credentials");
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
}
