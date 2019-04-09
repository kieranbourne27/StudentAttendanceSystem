<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="main.css">
</head>
<body>
    <%
        if (session.getAttribute("username") != null) {
    %>
    <div class="footer">
       <form method="POST" action="LogoutService.do" >
          <button type="submit" name="tbl" value="List" class="btn-link">Logout</button>
       </form>
    </div>
    <%}%>
</body>

