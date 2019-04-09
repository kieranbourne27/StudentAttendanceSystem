<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="main.css">
        <title>View Attendance</title>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <div class = "main">
            <h2>Your Sessions</h2>
            <h3>Select a session you want to view attendance for</h3>
            <div class = "displaySessions">
                <%=(String)(request.getAttribute("sessionTable"))%>
            </div>
            <% 
            if (!session.getAttribute("userType").equals("Student")) {
            %>
            <h3>Enter the session reference to view details and attendance records for that session</h3>
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
            <%
             if (session.getAttribute("sessionReferenceError") != null) {
                 out.print(session.getAttribute("sessionReferenceError"));
             }  
            %>
            <%
            if ((String)(session.getAttribute("studentAttendanceTable"))!= null) {
            %>
            <div class = "displayStudentAttendance">
                <h3>Student Attendance Details</h3>
                <%=((String)(session.getAttribute("studentAttendanceTable"))!= null) ? (String)(session.getAttribute("studentAttendanceTable")):""%>
            </div>
            <br>
            
            <%
                if ((List<String[]>) session.getAttribute("attendanceDetails") != null) {
                Gson gsonObj = new Gson();
                Map<Object,Object> map = null;
                List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();

                List<String[]> attendanceDetails = (List<String[]>) session.getAttribute("attendanceDetails");

                for (String[] record : attendanceDetails) {
                    map = new HashMap<Object,Object>(); 
                    map.put("label", record[0] + "(" + record[1] + ")"); 
                    map.put("y", Double.parseDouble(record[2])); 
                    list.add(map);
                }
                String dataPoints = gsonObj.toJson(list);
            %>
            <script>
                function loadBarChart() { 

                var chart = new CanvasJS.Chart("chartContainer", {
                        theme: "light2",
                        title: {
                            text: "Attendance Summary For <%=session.getAttribute("selectedModuleName")%>"
                        },
                        subtitles: [{
                                text: "Details of student attendance percentages"
                        }],
                        axisY: {
                                title: "Attendance (%)",
                                labelFormatter: addSymbols
                        },
                        data: [{
                                type: "bar",
                                indexLabel: "{y}",
                                indexLabelFontColor: "#444",
                                indexLabelPlacement: "inside",
                                dataPoints: <%out.print(dataPoints);%>
                        }]
                });
                chart.render();

                function addSymbols(e) {
                        var suffixes = ["", "K", "M", "B"];

                        var order = Math.max(Math.floor(Math.log(e.value) / Math.log(1000)), 0);
                        if(order > suffixes.length - 1)
                        order = suffixes.length - 1;

                        var suffix = suffixes[order];
                        return CanvasJS.formatNumber(e.value / Math.pow(1000, order)) + suffix;
                }

                }
            </script>
            <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
            <div id = "chartContainer" class = "chartContainer">
                <script>
                    loadBarChart();
                </script>
            </div>
            
            <%
                }
            }
            %>
            <hr>
            <%
            if ((String)(request.getAttribute("attendanceRecordsTable"))!= null) {
            %>
                <div>
                <div class = "displayAttendanceRecords">
                <h3>Attendance Records</h3>
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
                <%
                if (session.getAttribute("attendanceRecordReferenceError") != null) {
                    out.print(session.getAttribute("attendanceRecordReferenceError"));
                }  
                %>
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
            <%}
            } else {
            %>
            <h3>Enter the session reference to view your attendance for that session</h3>
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
            <%
             if (session.getAttribute("sessionReferenceError") != null) {
                 out.print(session.getAttribute("sessionReferenceError"));
             }  
            %>
            <hr>
            <div class = "displayUserAttendance">
                <%=((String)(request.getAttribute("userAttendanceTable"))!= null) ? (String)(request.getAttribute("userAttendanceTable")):""%>
            </div>
            <br>
                <%
                    if ((String) session.getAttribute("studentAttendancePercentage") != null) {
                        Gson gsonObj = new Gson();
                        Map<Object,Object> map = null;
                        List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();

                        map = new HashMap<Object, Object>(); 
                        map.put("label", "Session Attended"); 
                        map.put("y", Double.parseDouble((String) session.getAttribute("studentAttendancePercentage"))); 
                        map.put("exploded", true); 
                        list.add(map);

                        map = new HashMap<Object,Object>(); 
                        map.put("label", "Sessions not Attended"); 
                        map.put("y", Double.parseDouble("100") - Double.parseDouble((String) session.getAttribute("studentAttendancePercentage"))); 
                        list.add(map);

                        String dataPoints = gsonObj.toJson(list);
                %>
            			
            <script>
                function loadChart() { 
                    var chart = new CanvasJS.Chart("chartContainer", {
                            theme: "light2",
                            animationEnabled: true,
                            title:{
                                text: "Attendance overview for <%=session.getAttribute("selectedModuleName")%>"
                            },
                            data: [{
                                    type: "pie",
                                    showInLegend: true,
                                    legendText: "{label}",
                                    toolTipContent: "{label}: <strong>{y}%</strong>",
                                    indexLabel: "{label} {y}%",
                                    dataPoints : <%out.print(dataPoints);%>
                            }]
                    });

                    chart.render();

                }
            </script>
            <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
            <div id = "chartContainer" class = "chartContainer">
                <script>
                    loadChart();
                </script>
            </div>
            <%
                    }
            }
            %>
        </div>
        <br>
        <jsp:include page="foot.jsp"/>
    </body>
</html>
