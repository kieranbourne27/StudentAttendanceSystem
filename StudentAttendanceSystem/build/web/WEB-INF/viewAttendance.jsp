<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>View Attendance</title>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <h2>Your Sessions</h2>
        <h3>Select a session you want to view attendance for</h3>
        <div class = "displaySessions">
            <%=(String)(request.getAttribute("sessionTable"))%>
        </div>
        <h3>Enter the session reference to view attendance records for that session</h3>
        <form method="POST" action="AttendanceHandler.do">
            <table id="selectSessionTable">
                <tbody>
                    <tr>
                        <td style="padding-left: 0px">Session Reference:</td>
                        <td><input type="text" name="sessionReference" required/></td>
                        <td><button type="submit" name="tbl" value="ViewAttendance">Select</button></td>
                    </tr>
                </tbody>
            </table>
        </form>
        <hr>
        <%
        if ((String)(request.getAttribute("attendanceRecordsTable"))!= null) {
        %>
            <div>
            <div class = "displayAttendanceRecords">
            <%=((String)(request.getAttribute("attendanceRecordsTable"))!= null) ? (String)(request.getAttribute("attendanceRecordsTable")):""%>
            </div>
            <h3>Enter the attendance record reference to view attendees</h3>
            <form method="POST" action="AttendanceHandler.do">
                <table id="selectSessionTable">
                    <tbody>
                        <tr>
                            <td style="padding-left: 0px">Attendance Record Reference:</td>
                            <td><input type="text" name="attendanceRecordReference" required/></td>
                            <td><button type="submit" name="tbl" value="ViewAttendees">Select</button></td>
                        </tr>
                    </tbody>
                </table>
            </form>
            <hr>
        </div>
        <%}%>
        <%
        if ((String)(request.getAttribute("attendeesTable"))!= null) {
        %>
        <div>
            <h2>Students present for this session</h2>
            <div class = "displayAttendeeRecords">
            <%=request.getAttribute("attendeesTable")%>
            </div>
            <hr>
        </div>
        <%}%>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
