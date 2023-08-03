<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="sample.Customer"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title>Table with Edit and Delete Options</title>
</head>
<body>
    <h1>Table with Edit and Delete Options</h1>
    
    <!-- Access the customersList attribute directly from the session -->
    <c:set var="cList" value="${sessionScope.customersList}" />
    
    <!-- Check if the cList is not null before displaying the data -->
    <c:if test="${not empty cList}">
        <table id="dataTable" border="1">
            <thead>
                <tr>
                    <th>uuid</th>
                    <th>first_name</th>
                    <th>last_name</th>
                    <th>email</th>
                    <th>phone</th>
                </tr>
            </thead>
            <tbody id="tableBody">
                <!-- Table data will be dynamically added here -->
                <c:forEach items="${cList}" var="customer">
                    <tr>
                        <td>${customer.uuid}</td>
                        <td>${customer.first_name}</td>
                        <td>${customer.last_name}</td>
                        <td>${customer.email}</td>
                        <td>${customer.phone}</td>
                        <td>
                        <button onclick="editRow('${customer.uuid}')">Edit</button>
                        <button onclick="deleteRow('${customer.uuid}')">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
    <a href="AddUser.html">
    <button onclick="AddNew()">Add New</button></a>
  </body>  

    <script>
        // Function to handle edit action for a row
        
        function AddNew()
        {
        	  
        	alert("Adding new user Now");
            var xhr = new XMLHttpRequest();
            xhr.open("GET","AddNew", true);

            // Set up the callback function to handle the server response
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    console.log(xhr.responseText);
                }
            };

            // Send the request
            xhr.send();
            
        	
        	
        }
        function editRow(id) {
            // Replace this line with the appropriate edit logic based on your needs
            alert("Edit row with ID: " + id);
        }

        // Function to handle delete action for a row
        function deleteRow(id) {
            // Replace this line with the appropriate delete logic based on your needs
            alert("Delete row with ID: " + id);
            
            var xhr = new XMLHttpRequest();

            // Configure the GET request to the Servlet that will handle the Java class invocation
             xhr.open("GET","DeleteCustomer?id=" + encodeURIComponent(id), true);

            // Set up the callback function to handle the server response
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    console.log(xhr.responseText);
                }
            };

            // Send the request
            xhr.send();
            console.log("Reloading the page");
            location.reload();
        }
    </script>

</html>
