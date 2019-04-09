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
public class SessionAction extends HttpServlet {

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
            case "Create": 
                if (session.getAttribute("userType").equals("Admin")) {
                    obtainLecturers(session, request, response);
                }
                request.getRequestDispatcher("/WEB-INF/createSession.jsp").forward(request, response); 
                break;
            case "Edit":
                List<String> sessionList = (List<String>) session.getAttribute("sessionList");
                if (sessionList.contains(request.getParameter("sessionReference"))) {
                    String[] sessionDetails = dbBean.retrieveSessionDetails(request.getParameter("sessionReference"));
                    List<String> registeredStudents = dbBean.getRegisteredStudents(request.getParameter("sessionReference"));
                    List<String> availableStudents = dbBean.getAvailableStudents(registeredStudents);
                    request.setAttribute("module", sessionDetails[0]);
                    request.setAttribute("room", sessionDetails[1]);
                    request.setAttribute("time", sessionDetails[2]);
                    request.setAttribute("registeredStudents", registeredStudents);
                    request.setAttribute("availableStudents", availableStudents);
                    request.setAttribute("sessionReference", request.getParameter("sessionReference"));
                    request.getRequestDispatcher("/WEB-INF/editSession.jsp").forward(request, response);
                } else {
                    session.setAttribute("sessionReferenceError", "No data found for session with that reference");
                    getSessionsForUser(session, request, response);
                }
                break;
        }
    }
    
    private void getSessionsForUser(HttpSession session,  HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String msg = "No sessions";
        String query = "SELECT MODULE, ROOM, TIME, REFERENCE FROM SESSION "
                + "JOIN USERS ON USERS.ID = SESSION.OWNER_ID "
                + "WHERE USERS.USERNAME = '" + session.getAttribute("username") + "'";
        if (session.getAttribute("userType").equals("Admin")) {
            query = "SELECT MODULE, ROOM, TIME, REFERENCE FROM SESSION";
        }
        String result = requestData(session, msg, query);
        
        request.setAttribute("sessionTable", result);
        request.getRequestDispatcher("/WEB-INF/manageSessions.jsp").forward(request, response);
    }
    
    private void obtainLecturers(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Jdbc jdbc = (Jdbc) session.getAttribute("dbbean");
        String query = "SELECT USERNAME FROM USERS WHERE USERTYPE = 'Lecturer'";
        String[] lecturers = requestQueryWithStringArray(session, query);
        
        if(lecturers[0] != null){
            request.setAttribute("lecturers", lecturers);
        }else{
            request.setAttribute("message", "Could not retrieve lecturers.");
        }
    }
    
    private String[] requestQueryWithStringArray(HttpSession session, String qry) {
      String[] result = null;
      Jdbc dbBean = (Jdbc) session.getAttribute("dbbean");
      result = dbBean.retrieveQueryWithStringArray(qry);

      return result;
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
            Logger.getLogger(SessionAction.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SessionAction.class.getName()).log(Level.SEVERE, null, ex);
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
