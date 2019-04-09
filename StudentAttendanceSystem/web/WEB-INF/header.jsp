<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="main.css">
</head>
<div class="header">
    <form method="POST" action="UserService.do">
 <%
     if (session.getAttribute("userType").toString().trim().equals("Admin")) {
 %>
        <button type="submit" name="tbl" value="List" class="btn-link"> List Users</button>
        <button type="submit" name="tbl" value="NewUser" class="btn-link"> Create User</button>
        <button type="submit" name="tbl" value="Delete" class="btn-link"> Delete User</button>
        <button type="submit" name="tbl" value="ManageSessions" class="btn-link"> Manage Sessions</button>
        <button type="submit" name="tbl" value="Update" class="btn-link"> Change Password</button>
    </form>
<%}else if (session.getAttribute("userType").toString().trim().equals("Lecturer")) {%>
    <form method="POST" action="UserService.do">
        <button type="submit" name="tbl" value="ManageSessions" class="btn-link">Manage Sessions</button>
        <button type="submit" name="tbl" value="TakeAttendance" class="btn-link">Take Attendance</button>
        <button type="submit" name="tbl" value="ViewAttendance" class="btn-link">View Attendance</button>
        <button type="submit" name="tbl" value="Update" class="btn-link">Change Password</button>
    </form>
<%}else if (session.getAttribute("userType").toString().trim().equals("Student")) {%>
    <form method="POST" action="UserService.do">
        <button type="submit" name="tbl" value="ViewAttendance" class="btn-link">View Attendance</button>
        <button type="submit" name="tbl" value="Update" class="btn-link">Edit Details</button>
    </form>
<%}%>
 </div></br></br>

