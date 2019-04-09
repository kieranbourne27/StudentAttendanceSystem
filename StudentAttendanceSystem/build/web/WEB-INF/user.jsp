<%@page import="model.Jdbc"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>Create a New User</title>
    </head>
    <body onload="displayStudentNumberInput()">
<%
    if(session.getAttribute("username") == null){
        response.sendRedirect("index.jsp");
        return;
    }
%>
        <jsp:include page="header.jsp"/>
        <%! int i=0;
            String str="Register"; 
            String url = "NewUser.do";
        %>
        <%
            String[] userNames = (String[])request.getAttribute("userNames");
            if((String)request.getAttribute("msg")=="del") {
                str = "Delete";
                url = "Delete.do";
        %>
        <h1>Delete A User</h1>
        <h2>Please select the username of the user you wish to delete</h2>
        <form method="POST" action="<%=url%>">     
            <table>
                <tr>
                    <td>
                        <select name="username">
                            <%
                                for(int i = 0; i < userNames.length; i++){
                                    String name = userNames[i];
                            %>
                            <option value = " <%=(String)(name)%> "><%=(String)(name)%></option>
                            <% } %>
                        </select>
                    </td>
                </tr>
                <tr> 
                    <td> <input type="submit" value="<%=str%>"/></td>
                </tr>
            </table>
        </form> 
        <%
            }
            else {
                str="Register";
                url = "NewUser.do";
        %>
        <h1>Create A User</h1>
        <h2>Please enter the following details for the user</h2>
        <form method="POST" action="<%=url%>">     
            <table id="newUserTable">
                <tbody>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="username" required/></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="password" name="password" required/></td>
                    </tr>
                    <tr>
                        <td>Name:</td>
                        <td><input type="text" name="fullName" required/></td>
                    </tr>
                    <tr>
                        <td>User Type (Admin, Lecturer, Student):</td>
                        <td>
                            <select id="userType" name="userType" onchange="displayStudentNumberInput()" required>
                                <option value="Admin">Admin</option>
                                <option value="Lecturer">Lecturer</option>
                                <option value="Student">Student</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="studentNumberRow">
                        <td>Student Number:</td>
                        <td><input type="text" name="studentNumber" required</td>
                    </tr>
                    <tr>
                        <td>I understand that my personal data will be held and used within the attendance system</td>
                        <td><input type="checkbox" required=""></td>
                    </tr>
                </tbody
                <tr> 
                    <td> <input type="submit" value="<%=str%>"/></td>
                </tr>
            </table>
        </form>
        <%
            } 
        %>
        <p><%
                if (request.getAttribute("message") != null) {
                    out.println(request.getAttribute("message"));
                }
            %></p>
        </br>
        <jsp:include page="foot.jsp"/>
        
        <script>
                   function displayStudentNumberInput() {
                        var e = document.getElementById("userType");
                        var selectedUserType = e.options[e.selectedIndex].value;
                        if (selectedUserType === "Student") {
                            document.getElementById("studentNumberRow").style.visibility = "visible";
                        } else {
                            document.getElementById("studentNumberRow").style.visibility = "hidden";
                        }
                   }
        </script>
    </body>
</html>
