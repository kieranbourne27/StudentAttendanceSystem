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
                if (session.getAttribute("attendanceList") == null) {
                    List<String> attendanceList = new ArrayList();
                    session.setAttribute("attendanceList", attendanceList);
                }
                List<String> attendanceList = (List) session.getAttribute("attendanceList");
                attendanceList.add(request.getParameter("studentNumber"));
                request.setAttribute("clearAttendees", "false");
                request.getRequestDispatcher("/WEB-INF/recordAttendance.jsp").forward(request, response);
                break;
            case "SubmitRegister":
                attendanceList = (List) session.getAttribute("attendanceList");
                boolean result = dbBean.createAttendanceRecord((String) session.getAttribute("module"), (String) session.getAttribute("sessionReference"));
                String attendanceRecordID = dbBean.getLatestAttendanceRecordID();
                dbBean.generateAttendeeRecords(attendanceList, attendanceRecordID);
                if (result){
                    request.setAttribute("msg", "Register successfully submitted");
                } else {
                    request.setAttribute("msg", "Register failed to be submitted");
                }
                getSessionsForUser(session, request, response);
                request.getRequestDispatcher("/WEB-INF/takeAttendance.jsp").forward(request, response);
                break;
            case "ViewAttendance":
                String sessionID = dbBean.getSessionIDFromReference(request.getParameter("sessionReference"));
                String attendanceRecords = dbBean.getAttendanceRecordsForSession(sessionID, session);
                request.setAttribute("attendanceRecordsTable", attendanceRecords);
                session.setAttribute("attendanceRecordsTable", attendanceRecords);
                getSessionsForUser(session, request, response);
                break;
            case "ViewAttendees":
                request.setAttribute("attendanceRecordsTable", session.getAttribute("attendanceRecordsTable"));
                String selectedAttendanceRecordID = dbBean.getAttendanceRecordIDFromReference(request.getParameter("attendanceRecordReference"));
                String attendees = dbBean.getAttendeesForAttendanceRecord(selectedAttendanceRecordID, session);
                request.setAttribute("attendeesTable", attendees);
                getSessionsForUser(session, request, response);
                break;
            default:
                List<String> sessionDetails = Arrays.asList(dbBean.retrieveSessionDetails(request.getParameter("sessionReference")));
                session.setAttribute("module", sessionDetails.get(0));
                session.setAttribute("room", sessionDetails.get(1));
                session.setAttribute("time", sessionDetails.get(2));
                session.setAttribute("sessionReference", request.getParameter("sessionReference"));
                request.setAttribute("clearAttendees", "true");
                request.getRequestDispatcher("/WEB-INF/recordAttendance.jsp").forward(request, response);
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
        if (request.getParameter("tbl").equals("ManageSessions")) {
            request.getRequestDispatcher("/WEB-INF/manageSessions.jsp").forward(request, response);
        } else if (request.getParameter("tbl").equals("TakeAttendance")) {
            request.getRequestDispatcher("/WEB-INF/takeAttendance.jsp").forward(request, response);
        } else  {
            request.getRequestDispatcher("/WEB-INF/viewAttendance.jsp").forward(request, response);
        }
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
