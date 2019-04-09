<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>Change Password</title>
    </head>
    <body>
    <%
        if(session.getAttribute("username") == null){
            response.sendRedirect("index.jsp");
        }
    %>
        <jsp:include page="header.jsp"/>
        <h2>Password change</h1>
        <h3>Please enter and confirm your new password</h2>
           <form method="POST" action="Update.do">     
            <table>
                <tr>
                    <td>New Password:</td>
                    <td><input type="password" name="password" required/></td>
                </tr>
                <tr>
                    <td>Confirm Password:</td>
                    <td><input type="password" name="newpasswd" required/></td>
                </tr>
                <tr> 
                    <td> <input type="submit" value="Change"/></td>
                </tr>
            </table>
        </form>
        <%=((String)(request.getAttribute("msg"))!=null) ? (String)(request.getAttribute("msg")):""%>
        <br>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
