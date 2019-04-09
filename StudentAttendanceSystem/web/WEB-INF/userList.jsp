<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>List of users</title>
    </head>
    <body>
        <%
            if(session.getAttribute("username") == null){
                response.sendRedirect("index.jsp");
            }
        %>
        <jsp:include page="header.jsp"/>
        <h1>List of all users:</h1>
        <div class = "displayUsers">
            <%=(String)(request.getAttribute("userList"))%>
        </div>
        <br>
        <jsp:include page="foot.jsp"/>
    </body>
</html>