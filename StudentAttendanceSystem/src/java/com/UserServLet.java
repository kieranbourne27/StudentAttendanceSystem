/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
 * @author me-aydin
 */
public class UserServLet extends HttpServlet {

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

        HttpSession session = request.getSession(false);
        
        Jdbc dbBean = new Jdbc();
        dbBean.connect((Connection) request.getServletContext().getAttribute("connection"));
        
        if ((Connection) request.getServletContext().getAttribute("connection") == null) {
            request.getRequestDispatcher("/WEB-INF/conErr.jsp").forward(request, response);
        }
        
        if(session == null){
            response.sendRedirect("index.jsp");
            return;
        }
                
        response.setContentType("text/html;charset=UTF-8");

        if ((Connection) request.getServletContext().getAttribute("connection") == null) {
            request.getRequestDispatcher("/WEB-INF/conErr.jsp").forward(request, response);
        }
        
        switch(request.getParameter("tbl")){
            case "List":
                listUsersAndUserTypes(session, request, response);
                break;
            case "NewUser":
                request.getRequestDispatcher("/WEB-INF/user.jsp").forward(request, response);
                break;
            case "Update":
                request.getRequestDispatcher("/WEB-INF/passwdChange.jsp").forward(request, response);
                break;
            case "Login":
                request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                break;
            case "Delete":
                request.setAttribute("msg", "del");
                obtainPresentUsers(session, request, response);
                break;
            case "ManageSessions":
                session.removeAttribute("sessionReferenceError");
                if (session.getAttribute("userType").equals("Admin")) {
                    getSessionsForAdmin(session, request, response);
                } else {
                    getSessionsForUser(session, request, response);
                }
                break;
            case "TakeAttendance":
                session.removeAttribute("sessionReferenceError");
                getSessionsForUser(session, request, response);
                break;
            case "ViewAttendance":
                session.removeAttribute("sessionReferenceError");
                if (session.getAttribute("userType").equals("Lecturer")) {
                    session.removeAttribute("studentAttendanceTable");
                    getSessionsForUser(session, request, response);
                } else if (session.getAttribute("userType").equals("Student")) {
                    session.removeAttribute("studentAttendancePercentage");
                    String userID = dbBean.retrieveCurrentUserId(session);
                    getSessionsForStudent(session, request, response, userID);
                }
                break;
            default:
                request.getRequestDispatcher("/WEB-INF/portal.jsp").forward(request, response);
                break;
        }
    }
    
    private void obtainPresentUsers(HttpSession session, HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException{
        Jdbc jdbc = (Jdbc) session.getAttribute("dbbean");
        String query = "SELECT username FROM users";
        String[] userNames = requestQueryWithStringArray(session, query);
        
        if(userNames[0] != null){
            request.setAttribute("userNames", userNames);
            request.getRequestDispatcher("/WEB-INF/user.jsp").forward(request, response);
        }else{
            request.setAttribute("message", "Could not retrieve usernames.");
            request.getRequestDispatcher("/WEB-INF/portal.jsp").forward(request, response);
        }
    }
    
    private void listUsersAndUserTypes(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String msg = "No users";
        String qry = "SELECT USERNAME, USERTYPE FROM USERS";
        String result = requestData(session, msg, qry);
        
        request.setAttribute("userList", result);
        request.getRequestDispatcher("/WEB-INF/userList.jsp").forward(request, response);
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
    
    private void getSessionsForAdmin(HttpSession session,  HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String msg = "No sessions";
        String query = "SELECT MODULE, ROOM, TIME, REFERENCE FROM SESSION";
        String result = requestData(session, msg, query);
        
        request.setAttribute("sessionTable", result);
        if (request.getParameter("tbl").equals("ManageSessions")) {
            request.getRequestDispatcher("/WEB-INF/manageSessions.jsp").forward(request, response);
        } else if (request.getParameter("tbl").equals("TakeAttendance")) {
            request.getRequestDispatcher("/WEB-INF/takeAttendance.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/viewAttendance.jsp").forward(request, response);
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
        } else {
            request.getRequestDispatcher("/WEB-INF/viewAttendance.jsp").forward(request, response);
        }
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
    
    private String[] requestQueryWithStringArray(HttpSession session, String qry) {
      String[] result = null;
      Jdbc dbBean = (Jdbc) session.getAttribute("dbbean");
      result = dbBean.retrieveQueryWithStringArray(qry);

      return result;
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
            Logger.getLogger(UserServLet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UserServLet.class.getName()).log(Level.SEVERE, null, ex);
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
