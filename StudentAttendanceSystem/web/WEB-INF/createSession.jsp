<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>Create a Session</title>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <h1>Please Enter Session Details:</h1>
        <form method="POST" action="CreateSession.do">     
            <table id="newSessionTable">
                <tbody>
                    <tr>
                        <td>Module:</td>
                        <td><input type="text" name="module" required/></td>
                    </tr>
                    <tr>
                        <td>Room:</td>
                        <td><input type="text" name="room" required/></td>
                    </tr>
                    <tr>
                        <td>Time:</td>
                        <td><input type="text" name="time" required/></td>
                    </tr>
                </tbody
                <tr> 
                    <td> <input type="submit" value="Submit"/></td>
                </tr>
            </table>
        </form>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
