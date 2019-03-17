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
public class UpdateSession extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        String[] query = captureSessionData(request);
        
        Jdbc dbBean = new Jdbc();
        dbBean.connect((Connection) request.getServletContext().getAttribute("connection"));
        session.setAttribute("dbbean", dbBean);
        updateSession(dbBean, query, request, session, response);
    }
    
    private void updateSession(Jdbc dbBean, String[] query, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException, ServletException {
        String updateQuery = "UPDATE SESSION SET MODULE = '" + query[0] 
                + "', ROOM = '" + query[1] 
                + "', TIME = '"+ query[2] 
                + "' WHERE REFERENCE = '" + request.getParameter("sessionReference") + "'";
        if (dbBean.updateTableWithQuery(updateQuery)) {            
            request.setAttribute("message", "Session has been updated");
            getSessionsForUser(session, request, response);
        }
        else {
            request.setAttribute("message", query[0] + " was not added.");
        }
    }
    
    private String[] captureSessionData(HttpServletRequest request) {
        String[] query = new String[5];
        query[0] = (String) request.getParameter("module");
        query[1] = (String) request.getParameter("room");
        query[2] = (String) request.getParameter("time");

        return query;
    }
    
    private void getSessionsForUser(HttpSession session,  HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String msg = "No sessions";
        String query = "SELECT MODULE, ROOM, TIME, REFERENCE FROM SESSION "
                + "JOIN USERS ON USERS.ID = SESSION.OWNER_ID "
                + "WHERE USERS.USERNAME = '" + session.getAttribute("username") + "'";
        String result = requestData(session, msg, query);
        
        request.setAttribute("sessionTable", result);
        request.getRequestDispatcher("/WEB-INF/manageSessions.jsp").forward(request, response);
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
        processRequest(request, response);
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
        processRequest(request, response);
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
