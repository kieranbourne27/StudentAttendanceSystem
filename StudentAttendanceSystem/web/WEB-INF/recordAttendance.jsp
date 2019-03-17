<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>Record Attendance</title>
    </head>
    <body>
        <script>
            function clearAttendees() {
                document.getElementById("registeredStudents").innerHTML = "";
            }
        </script>
        <jsp:include page="header.jsp"/>
        <div>
            <div>
                <h2>Recording Attendance for <%=session.getAttribute("module")%></h2>
                <form method="POST" action="AttendanceHandler.do">
                    <h3>Please scan the student card barcode and then submit</h3>
                    <table>
                        <tbody>
                            <tr>
                                <td style="padding-left: 0px">Student Number:</td>
                                <td><input type="text" name="studentNumber" required/></td>
                                <td><button type="submit" name="tbl" value="SubmitStudentNumber">Add</button></td>
                            </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <hr>
            <div id="registeredStudents" class="registeredStudents">
                <h3>Current students registered for this session</h3>
                <p>
                    <c:forEach items="${attendanceList}" var="attendee">
                        <c:out value="${attendee}" /><br>
                    </c:forEach>
                </p>
                <form method="POST" action="AttendanceHandler.do">
                    <button type="submit" name="tbl" value="SubmitRegister">Submit</button>
                </form>
            </div>
        </div>
        <%
            boolean clearAttendees = Boolean.parseBoolean((String) request.getAttribute("clearAttendees"));
            if (clearAttendees) {
                session.removeAttribute("attendanceList");
        %>
        <script>
            clearAttendees();
        </script>
        <%
            }
        %>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
