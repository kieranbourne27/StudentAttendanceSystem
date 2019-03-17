<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>Manage Sessions</title>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <h2>Your Sessions</h2>
        <h3>These are your current sessions</h3>
        <div class = "displayUsers">
            <%=(String)(request.getAttribute("sessionTable"))%>
        </div>
        <br>
        <form method="POST" action="SessionAction.do">
            <button type="submit" name="tbl" value="Create">Create</button>
            <p>
                <%
                    if (request.getAttribute("message") != null) {
                        out.println(request.getAttribute("message"));
                    }
                %>
            </p>
        </form>
        <form method="POST" action="SessionAction.do">
            <hr>
            <h2>Edit a Session</h2>
            <h3>Enter the reference of the session you want to edit</h3>
            <table id="editSessionTable">
                <tbody>
                    <tr>
                        <td style="padding-left: 0px">Session Reference:</td>
                        <td><input type="text" name="sessionReference" required/></td>
                        <td><button type="submit" name="tbl" value="Edit">Edit</button></td>
                    </tr>
                </tbody>
            </table>
        </form>
        <form method="POST" action="SessionAction.do">
            <hr>
            <h2>Delete a Session</h2>
            <h3>Enter the reference of the session you want to delete</h3>
            <table id="deleteSessionTable">
                <tbody>
                    <tr>
                        <td style="padding-left: 0px">Session Reference:</td>
                        <td><input type="text" name="sessionReference" required/></td>
                        <td><button type="submit" name="tbl" value="Delete">Delete</button></td>
                    </tr>
                </tbody>
            </table>
        </form>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
