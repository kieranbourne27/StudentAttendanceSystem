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
        <h1>Session Details:</h1>
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
                    <td> <input type="submit" value="Update"/></td>
                </tr>
            </table>
            <input type="hidden" name="sessionReference" value="<%=request.getAttribute("sessionReference")%>" />
        </form>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
