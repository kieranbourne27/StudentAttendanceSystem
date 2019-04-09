/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pages;

import com.UserServLet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Jdbc;

/**
 *
 * @author Kieran
 */
public class AttendanceHandler extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        
        Jdbc dbBean = new Jdbc();
        dbBean.connect((Connection) request.getServletContext().getAttribute("connection"));
        
        if ((Connection) request.getServletContext().getAttribute("connection") == null) {
            request.getRequestDispatcher("/WEB-INF/conErr.jsp").forward(request, response);
        }
        
        switch(request.getParameter("tbl")){
            case "SubmitStudentNumber":
                List<String> registeredStudents = dbBean.getRegisteredStudents((String) session.getAttribute("sessionReference"));
                if (session.getAttribute("attendanceList") == null) {
                    List<String> attendanceList = new ArrayList();
                    session.setAttribute("attendanceList", attendanceList);
                }
                List<String> attendanceList = (List) session.getAttribute("attendanceList");
                if (attendanceList.contains(request.getParameter("studentNumber"))) {
                    request.setAttribute("message", "Student has already been register");
                    request.setAttribute("clearAttendees", "false");
                    request.getRequestDispatcher("/WEB-INF/recordAttendance.jsp").forward(request, response);
                    break;
                }
                request.setAttribute("message", "Student is not registered for this session");
                for (String registeredStudent : registeredStudents) {
                    if (registeredStudent.contains((String) request.getParameter("studentNumber"))) {
                        attendanceList.add(request.getParameter("studentNumber"));
                        request.setAttribute("message", null);
                    } 
                }
                request.setAttribute("clearAttendees", "false");
                request.getRequestDispatcher("/WEB-INF/recordAttendance.jsp").forward(request, response);
                break;
            case "SubmitRegister":
                session.removeAttribute("sessionReferenceError");
                attendanceList = (List) session.getAttribute("attendanceList");
                boolean result = dbBean.createAttendanceRecord((String) session.getAttribute("module"), (String) session.getAttribute("sessionReference"), 
                        (String) session.getAttribute("sessionDate"));
                String attendanceRecordID = dbBean.getLatestAttendanceRecordID();
                dbBean.generateAttendeeRecords(attendanceList, attendanceRecordID);
                dbBean.updateUserAttendanceRecords(attendanceList, (String) session.getAttribute("sessionReference"));
                if (result){
                    request.setAttribute("msg", "Register successfully submitted");
                } else {
                    request.setAttribute("msg", "Register failed to be submitted");
                }
                session.removeAttribute("attendanceDetails");
                getSessionsForUser(session, request, response);
                request.getRequestDispatcher("/WEB-INF/takeAttendance.jsp").forward(request, response);
                break;
            case "ViewAttendance":
                List<String> sessionList = (List<String>) session.getAttribute("sessionList");
                if (request.getParameter("sessionReference") != null && sessionList.contains(request.getParameter("sessionReference"))) {
                    session.removeAttribute("sessionReferenceError");
                    session.removeAttribute("attendanceRecordReferenceError");
                    if (session.getAttribute("userType").equals("Lecturer")) {
                        String msg = "No attendance data present for this session";
                        String sessionID = dbBean.getSessionIDFromReference(request.getParameter("sessionReference"));
                        session.setAttribute("sessionID", sessionID);
                        session.setAttribute("selectedModuleName", dbBean.getModuleFromReference(request.getParameter("sessionReference")));
                        String attendanceRecords = dbBean.getAttendanceRecordsForSession(sessionID, session);
                        request.setAttribute("attendanceRecordsTable", attendanceRecords);
                        session.setAttribute("attendanceRecordsTable", attendanceRecords);
                        String query = "SELECT NAME, STUDENTNUMBER, SESSIONS_ATTENDED, ATTENDANCE_PERCENTAGE FROM USER_ATTENDANCE " +
                        "JOIN USERS ON USER_ATTENDANCE.USER_ID = USERS.ID " +
                        "WHERE SESSION_ID = " + sessionID;
                        String requestResult = requestData(session, msg, query);
                        session.setAttribute("studentAttendanceTable", requestResult);
                        String attendanceDetailsQuery = "SELECT NAME, STUDENTNUMBER, ATTENDANCE_PERCENTAGE FROM USERS " +
                        "JOIN USER_ATTENDANCE ON USER_ATTENDANCE.USER_ID = USERS.ID " +
                        "WHERE USER_ATTENDANCE.SESSION_ID = " + sessionID;
                        requestData(session, "No attendance details found", attendanceDetailsQuery);
                        getSessionsForUser(session, request, response);
                        request.getRequestDispatcher("/WEB-INF/viewAttendance.jsp").forward(request, response);
                    } else if (session.getAttribute("userType").equals("Student")) {
                        String msg = "No attendance data present for this user";
                        String sessionID = dbBean.getSessionIDFromReference(request.getParameter("sessionReference"));
                        session.setAttribute("sessionID", sessionID);
                        session.setAttribute("selectedModuleName", dbBean.getModuleFromReference(request.getParameter("sessionReference")));
                        String userID = dbBean.retrieveCurrentUserId(session);
                        session.setAttribute("studentAttendancePercentage", 
                                dbBean.getStudentAttendancePercentage(sessionID, userID));
                        String query = "SELECT SESSIONS_ATTENDED, ATTENDANCE_PERCENTAGE FROM USER_ATTENDANCE WHERE USER_ID = " + userID + " AND SESSION_ID = " + sessionID;
                        String requestResult = requestData(session, msg, query);
                        request.setAttribute("userAttendanceTable", requestResult);
                        getSessionsForStudent(session, request, response, userID);
                    }
                } else {
                    session.setAttribute("sessionReferenceError", "No data found for session with that reference");
                    if (session.getAttribute("userType").equals("Student")) {
                        getSessionsForStudent(session, request, response, dbBean.retrieveCurrentUserId(session));
                    } else {
                        getSessionsForUser(session, request, response);
                        request.getRequestDispatcher("/WEB-INF/viewAttendance.jsp").forward(request, response);
                    }
                }
                break;
            case "ViewAttendees":
                List<String> attendanceRecordsList = (List<String>) session.getAttribute("attendanceRecordsList");
                if (request.getParameter("attendanceRecordReference") != null && attendanceRecordsList.contains(request.getParameter("attendanceRecordReference"))) {
                    request.setAttribute("attendanceRecordsTable", session.getAttribute("attendanceRecordsTable"));
                    String selectedAttendanceRecordID = dbBean.getAttendanceRecordIDFromReference(request.getParameter("attendanceRecordReference"));
                    String attendees = dbBean.getAttendeesForAttendanceRecord(selectedAttendanceRecordID, session);
                    request.setAttribute("attendeesTable", attendees);
                    getSessionsForUser(session, request, response);
                    request.getRequestDispatcher("/WEB-INF/viewAttendance.jsp").forward(request, response);
                } else {
                    session.setAttribute("attendanceRecordReferenceError", "No data found for attendance record with that reference");
                    request.setAttribute("attendanceRecordsTable", session.getAttribute("attendanceRecordsTable"));
                    getSessionsForUser(session, request, response);
                    request.getRequestDispatcher("/WEB-INF/viewAttendance.jsp").forward(request, response);
                }
                break;
            default:
                sessionList = (List<String>) session.getAttribute("sessionList");
                if (sessionList.contains(request.getParameter("sessionReference"))) {
                // Taking a register
                List<String> sessionDetails = Arrays.asList(dbBean.retrieveSessionDetails(request.getParameter("sessionReference")));
                session.setAttribute("module", sessionDetails.get(0));
                session.setAttribute("room", sessionDetails.get(1));
                session.setAttribute("time", sessionDetails.get(2));
                session.setAttribute("sessionReference", request.getParameter("sessionReference"));
                session.setAttribute("sessionDate", request.getParameter("sessionDate"));
                request.setAttribute("clearAttendees", "true");
                request.getRequestDispatcher("/WEB-INF/recordAttendance.jsp").forward(request, response);
                } else {
                    session.setAttribute("sessionReferenceError", "No data found for session with that reference");
                    request.setAttribute("nextPage", "TakeAttendance");
                    getSessionsForUser(session, request, response);
                    request.getRequestDispatcher("/WEB-INF/takeAttendance.jsp").forward(request, response);
                }
                break;
        }
    }
    
    private void getSessionsForUser(HttpSession session,  HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String msg = "No sessions";
        String query = "SELECT MODULE, ROOM, TIME, REFERENCE FROM SESSION "
                + "JOIN USERS ON USERS.ID = SESSION.OWNER_ID "
                + "WHERE USERS.USERNAME = '" + session.getAttribute("username") + "'";
        String result = requestData(session, msg, query);
        
        request.setAttribute("sessionTable", result);
    }
    
    private void getSessionsForStudent(HttpSession session,  HttpServletRequest request, HttpServletResponse response, String userID) throws ServletException, IOException {
        String msg = "No sessions";
        String query = "SELECT MODULE, ROOM, TIME, REFERENCE FROM REGISTEREDSTUDENTS " +
        "JOIN SESSION ON SESSION.ID = REGISTEREDSTUDENTS.SESSION_ID " +
        "WHERE REGISTEREDSTUDENTS.USER_ID = " + Integer.parseInt(userID);
        String result = requestData(session, msg, query);
        request.setAttribute("sessionTable", result);
        request.getRequestDispatcher("/WEB-INF/viewAttendance.jsp").forward(request, response);
    }
    
    private String requestData(HttpSession session, String msg, String qry) {
        try {
            Jdbc dbBean = (Jdbc) session.getAttribute("dbbean");
            msg = dbBean.retrieve(qry, session);
        } catch (SQLException ex) {
            Logger.getLogger(UserServLet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msg;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
