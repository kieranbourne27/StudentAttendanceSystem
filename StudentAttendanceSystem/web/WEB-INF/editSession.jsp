<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>Session Editor</title>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <h2>Session Details:</h1>
        <form method="POST" action="UpdateSession.do">
            <table id="editSessionTable">
                <tbody>
                    <tr>
                        <td>Module:</td>
                        <td><input type="text" name="module" value="<%=request.getAttribute("module")%>" required/></td>
                    </tr>
                    <tr>
                        <td>Room:</td>
                        <td><input type="text" name="room" value="<%=request.getAttribute("room")%>" required/></td>
                    </tr>
                    <tr>
                        <td>Time:</td>
                        <td><input type="text" name="time" value="<%=request.getAttribute("time")%>" required/></td>
                    </tr>
                </tbody
                <tr> 
                    <td><input type="submit" value="Update" name="Update"/></td>
                </tr>
            </table>
            <input type="hidden" name="sessionReference" value="<%=request.getAttribute("sessionReference")%>" />
        </form>
        <hr>
        <div id="registeredStudents">
            <h3>Current students registered for this session</h3>
            <p>
                <c:forEach items="${registeredStudents}" var="student">
                    <c:out value="${student}" /><br>
                </c:forEach>
            </p>
        </div>
        <form method="POST" action="UpdateSession.do">
            <div id="availableStudents">
                <h3>Students who can be registered for this session</h3>
                <select name="student">
                    <c:forEach items="${availableStudents}" var="availStudent">
                        <option value="${availStudent}"><c:out value="${availStudent}" /></option>
                    </c:forEach>
                </select>
                <input type="submit" value="Add" name="Update"/>
                <p>
                <%
                    if (request.getAttribute("message") != null) {
                        out.println(request.getAttribute("message"));
                    }
                %>
                </p>
                <input type="hidden" name="sessionReference" value="<%=request.getAttribute("sessionReference")%>" />
            </div>
        </form>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
