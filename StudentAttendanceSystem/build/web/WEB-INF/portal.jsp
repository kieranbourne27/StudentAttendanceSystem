<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>Portal</title>
    </head>
    <body>
<%
    if(session.getAttribute("username") == null){
        response.sendRedirect("index.jsp");
    }
%>
        
        <jsp:include page="header.jsp"/>
        <p><b><%
        if (session.getAttribute("username") != null) {
            out.println("Currently logged in as: " + session.getAttribute("username"));
        }
        %></b></p>
        <h1>Welcome to the portal page</h1>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
