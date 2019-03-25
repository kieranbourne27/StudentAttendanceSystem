<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>Take Attendance</title>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <h2>Your Sessions</h2>
        <h3>Select session you want to record attendance for</h3>
        <div class = "displaySessions">
            <%=(String)(request.getAttribute("sessionTable"))%>
        </div>
        <h3>Enter the session reference to start taking attendance</h3>
        <form method="POST" action="AttendanceHandler.do">
            <table id="selectSessionTable">
                <tbody>
                    <tr>
                        <td style="padding-left: 0px">Session Reference:</td>
                        <td><input type="text" name="sessionReference" required/></td>
                        <td><button type="submit" name="tbl" value="Select">Select</button></td>
                    </tr>
                </tbody>
            </table>
        </form>
        <hr>
        <%=((String)(request.getAttribute("msg"))!=null) ? (String)(request.getAttribute("msg")):""%>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
