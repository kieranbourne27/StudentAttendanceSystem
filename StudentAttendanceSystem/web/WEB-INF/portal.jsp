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
        <h1>Welcome to the student attendance system</h1>
        <h2>About</h2>
        <p style="font-size: large">The student attendance system has been developed to provide lecturers and students with a system that allows them to monitor and track attendance.<br>
           Users can make use of dynamic graphs that provide a visual representation of attendance figures contained within the system.
        </p>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
