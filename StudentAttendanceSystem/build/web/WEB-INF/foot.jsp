<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
 <%
     if (session.getAttribute("username") != null) {
 %>
 <div class="footer">
 <form method="POST" action="LogoutService.do" >
    <a href="index.jsp">Logout</a>
 </form>
 <!--<a href="/WEB-INF/portal.jsp"> Back to Main </a> -->
 </div>
 <%}%>

