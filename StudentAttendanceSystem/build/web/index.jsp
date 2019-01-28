<%-- 
    Document   : login
    Created on : 05-Nov-2018, 14:30:15
    Author     : k4-bourne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>Login Page</title>
    </head>
    <body style="background: url(https://cdn.escapismmagazine.com/gallery/5819c96c7f7b5.jpeg)">
        <div class="loginForm">
            <div class="loginLogo">
                <img src="https://idp.uwe.ac.uk/adfs/portal/logo/logo.png?id=883222B3C5782A4B95A781A48FDC3205D5818374AB3780112D5FC10A9E8900AE" 
                    alt="image" align="right">
            </div>
            
            <h1>Student Attendance System</h1>
            <h3>Please enter your login details</h3>
        
            <form method="POST" action="Login.do">
                <table>
                        <tr>
                            <td>Username:</td>
                            <td><input type="text" name="username"/></td>
                        </tr>
                        <tr>
                            <td>Password:</td>
                            <td><input type="password" name="password"/></td>
                        </tr>
                        <tr> 
                            <td> <input type="submit" value="Login"/></td>
                        </tr>
                </table>
            </form>
            <p> <a href ="signupUser.jsp">Forgotten password</a></p> 

            <p><%
                if (request.getAttribute("message") != null) {
                    out.println(request.getAttribute("message"));
                }
                %></p>
        </div>
    </body>
</html>
