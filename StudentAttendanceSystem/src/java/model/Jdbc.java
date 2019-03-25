package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import static java.sql.Types.NULL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

public class Jdbc {
    
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;    
    
    public Jdbc(String query){
        //this.query = query;
    }

    public Jdbc(){
    }
    
    public void connect(Connection con){
       connection = con;
    }
    
    private ArrayList rsToList() throws SQLException {
        ArrayList aList = new ArrayList();

        int cols = rs.getMetaData().getColumnCount();
        while (rs.next()) { 
          String[] s = new String[cols];
          for (int i = 1; i <= cols; i++) {
            s[i-1] = rs.getString(i);
          } 
          aList.add(s);
        } // while    
        return aList;
    } //rsToList
 
    private String makeTable(ArrayList list) {
        StringBuilder b = new StringBuilder();
        String[] row;
        b.append("<table border=\"3\">");
        for (Object s : list) {
          b.append("<tr>");
          row = (String[]) s;
            for (String row1 : row) {
                b.append("<td>");
                b.append(row1);
                b.append("</td>");
            }
          b.append("</tr>\n");
        } // for
        b.append("</table>");
        return b.toString();
    }//makeHtmlTable
    
    private String makeUsersTable(ArrayList list) {
        StringBuilder b = new StringBuilder();
        String[] row;
        b.append("<table border=\"3\">");
        
        b.append("<tr>");
        b.append("<th>Username</th>");
        b.append("<th>User Type</th>");
        b.append("<tr>");
        
        for (Object s : list) {
          b.append("<tr>");
          row = (String[]) s;
            for (String row1 : row) {
                b.append("<td>");
                b.append(row1);
                b.append("</td>");
            }
          b.append("</tr>\n");
        } // for
        b.append("</table>");
        return b.toString();
    }//makeHtmlTable
    
    private String makeSessionsTable(ArrayList list, HttpSession session) {
        StringBuilder b = new StringBuilder();
        String[] row;
        b.append("<table border=\"3\">");
        
        b.append("<tr>");
        b.append("<th>Module</th>");
        b.append("<th>Room</th>");
        b.append("<th>Time</th>");
        b.append("<th>Reference</th>");
        b.append("<tr>");
        
        for (Object s : list) {
          b.append("<tr>");
          row = (String[]) s;
            for (String row1 : row) {
                b.append("<td>");
                b.append(row1);
                b.append("</td>");
            }
          b.append("</tr>\n");
        } // for
        b.append("</table>");
        return b.toString();
    }//makeHtmlTable
    
    private String makeAttendanceRecordsTable(ArrayList list, HttpSession session) {
        StringBuilder b = new StringBuilder();
        String[] row;
        b.append("<table border=\"3\">");
        
        b.append("<tr>");
        b.append("<th>Reference</th>");
        b.append("<tr>");
        
        for (Object s : list) {
          b.append("<tr>");
          row = (String[]) s;
            for (String row1 : row) {
                b.append("<td>");
                b.append(row1);
                b.append("</td>");
            }
          b.append("</tr>\n");
        } // for
        b.append("</table>");
        return b.toString();
    }//makeHtmlTable
    
    private String makeAttendeesTable(ArrayList list, HttpSession session) {
        StringBuilder b = new StringBuilder();
        String[] row;
        b.append("<table border=\"3\">");
        
        b.append("<tr>");
        b.append("<th>Name</th>");
        b.append("<th>Student Number</th>");
        b.append("<tr>");
        
        for (Object s : list) {
          b.append("<tr>");
          row = (String[]) s;
            for (String row1 : row) {
                b.append("<td>");
                b.append(row1);
                b.append("</td>");
            }
          b.append("</tr>\n");
        } // for
        b.append("</table>");
        return b.toString();
    }//makeHtmlTable
    
    private void select(String query){
        //Statement statement = null;
        
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            //statement.close();
        }
        catch(SQLException e) {
            System.out.println("way way"+e);
            //results = e.toString();
        }
    }
    
    public String retrieveUserType(String username){
        String query = "SELECT USERTYPE FROM TEST.USERS Where UserName = '" + username + "'";
        String result = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            
            for (Object s : rsToList()) {
                String[] row = (String[]) s;
                for (String row1 : row) {
                    result = row1;
                }
            }
        }
        catch(SQLException e) {
            System.out.println("way way"+e);
            //results = e.toString();
        }
        return result;
    }
    
    public int retrieveNextID(){
        String query = "SELECT MAX(ID) FROM TEST.USERS";
        int result = 0;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            
            for (Object s : rsToList()) {
                String[] row = (String[]) s;
                for (String row1 : row) {
                    result = Integer.valueOf(row1) + 1;
                }
            }
        }
        catch(SQLException e) {
            System.out.println("way way"+e);
            //results = e.toString();
        }
        return result;
    }
    
    public String retrieveCurrentUserId(HttpSession session) throws SQLException{
        String result = "";
        String query = "SELECT ID FROM USERS WHERE USERNAME = '" + session.getAttribute("username") + "'";
        select(query);
        ArrayList<Object> ids = rsToList();
        
        if (ids == null || ids.isEmpty()) {
            System.out.println("No user found");
        } else if (ids.size() > 1) {
            System.out.println("More than one user identified");
        }
        
        for (Object s : ids) {
                String[] row = (String[]) s;
                for (String row1 : row) {
                    result = String.valueOf(row1);
                }
        }
        
        return result;
    }
    
    public int retrieveNextSessionID(){
        String query = "SELECT MAX(ID) FROM SESSION";
        int result = 0;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            
            for (Object s : rsToList()) {
                String[] row = (String[]) s;
                for (String row1 : row) {
                    result = Integer.valueOf(row1) + 1;
                }
            }
        }
        catch(SQLException e) {
            System.out.println("way way"+e);
            //results = e.toString();
        }
        return result;
    }
    
    public int retrieveNextAttendanceRecordID(){
        String query = "SELECT MAX(ID) FROM ATTENDANCE_RECORD";
        int result = 0;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            
            for (Object s : rsToList()) {
                String[] row = (String[]) s;
                for (String row1 : row) {
                    if (row1 == null) {
                        return 1;
                    } 
                    result = Integer.valueOf(row1) + 1;
                }
            }
        }
        catch(SQLException e) {
            System.out.println("way way"+e);
            //results = e.toString();
        }
        return result;
    }
    
    public int retrieveNextAttendeeRecordID(){
        String query = "SELECT MAX(ID) FROM ATTENDEE_RECORD";
        int result = 0;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            
            for (Object s : rsToList()) {
                String[] row = (String[]) s;
                for (String row1 : row) {
                    if (row1 == null) {
                        return 1;
                    } 
                    result = Integer.valueOf(row1) + 1;
                }
            }
        }
        catch(SQLException e) {
            System.out.println("way way"+e);
            //results = e.toString();
        }
        return result;
    }
    
    public String retrieve(String query, HttpSession session) throws SQLException {
        select(query);
        if (query.contains("users")) {
            return makeUsersTable(rsToList());
        } else if (query.contains("SELECT MODULE, ROOM, TIME, REFERENCE FROM SESSION")) {
            return makeSessionsTable(rsToList(), session);
        } else if (query.contains("SELECT REFERENCE FROM ATTENDANCE_RECORD")) {
            return makeAttendanceRecordsTable(rsToList(), session);
        } else if (query.contains("SELECT USERS.NAME, USERS.STUDENTNUMBER FROM ATTENDEE_RECORD")) {
            return makeAttendeesTable(rsToList(), session);
        }
        
        return makeTable(rsToList());
    }
    
    public String[] retrieveQueryWithStringArray(String query){
      return RunQuery(query);
    }
    
    public String[] retrieveSessionDetails(String sessionReference) {
        String query = "SELECT MODULE, ROOM, TIME FROM SESSION WHERE REFERENCE = '" + sessionReference + "'";
        String[] sessionDetails = retrieveQueryWithStringArray(query);
        
        return sessionDetails;
    }
    
    public String[] RunQuery(String qry){
      ArrayList<String> result = new ArrayList<>();
        try  {
            select(qry);
            
            for (Object s : rsToList()) {
                String[] row = (String[]) s;
                for (String row1 : row) {
                    result.add(row1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] results = result.toArray(new String[result.size()]);
        return results;
    }
    
    public boolean exists(String user) {
        boolean bool = false;
        try  {
            select("select username from users where username='"+user+"'");
            if(rs.next()) {
                System.out.println("TRUE");         
                bool = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bool;
    }
            
    public boolean insertUser(String[] str){
        PreparedStatement ps = null;
        boolean success = false;
        try {
            ps = connection.prepareStatement("INSERT INTO Users VALUES (?,?,?,?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, str[0].trim()); 
            ps.setString(2, str[1].trim());            
            ps.setString(3, str[2].trim());
            ps.setString(4, str[3].trim());
            ps.setString(5, String.valueOf(retrieveNextID()));
            success = ps.executeUpdate() != 0;
        
            ps.close();
            System.out.println("1 row added.");
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
         return success;
    }
    
    public boolean insertSession(String[] str, HttpSession session){
        PreparedStatement ps = null;
        boolean success = false;
        try {
            ps = connection.prepareStatement("INSERT INTO SESSION VALUES (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, String.valueOf(retrieveNextSessionID())); 
            ps.setString(2, str[0].trim());            
            ps.setString(3, str[1].trim());
            ps.setString(4, str[2].trim());
            ps.setString(5, retrieveCurrentUserId(session));
            ps.setString(6, generateSessionReference());
            success = ps.executeUpdate() != 0;
        
            ps.close();
            System.out.println("1 session added.");
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
         return success;
    }
    
    public String generateSessionReference() throws SQLException {
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(16); 
  
        for (int i = 0; i < 16; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        }
        
        if (!(validateReference(sb.toString()))) {
            generateSessionReference();
        }            
  
        return sb.toString(); 
    }
    
    public String generateAttendanceRecordReference() throws SQLException {
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(16); 
  
        for (int i = 0; i < 16; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        }
        
        if (!(validateAttendanceRecordReference(sb.toString()))) {
            generateAttendanceRecordReference();
        }            
  
        return sb.toString(); 
    }
    
    public boolean validateReference(String reference) throws SQLException {
        String query = "SELECT REFERENCE FROM SESSION";
        select(query);
        ArrayList<Object> references = rsToList();
        
        for (Object s : references) {
                String[] row = (String[]) s;
                for (String row1 : row) {
                    String currentRef = String.valueOf(row1);
                    // Check if reference has already been generated
                    if (currentRef.equals(reference)) {
                        return false;
                    }
                    
                }
        }
        
        return true;
    }
    
    public boolean validateAttendanceRecordReference(String reference) throws SQLException {
        String query = "SELECT REFERENCE FROM ATTENDANCE_RECORD";
        select(query);
        ArrayList<Object> references = rsToList();
        
        for (Object s : references) {
                String[] row = (String[]) s;
                for (String row1 : row) {
                    String currentRef = String.valueOf(row1);
                    // Check if reference has already been generated
                    if (currentRef.equals(reference)) {
                        return false;
                    }
                    
                }
        }
        
        return true;
    }
    
    public void update(String[] str) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("Update Users Set password=? where username=?",PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, str[1].trim()); 
            ps.setString(2, str[0].trim());
            ps.executeUpdate();
        
            ps.close();
            System.out.println("1 rows updated.");
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean updateTableWithQuery(String query) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            ps.close();
            System.out.println("1 rows updated.");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public void delete(String user){
       
      String query = "DELETE FROM Users " +
                   "WHERE username = '"+user.trim()+"'";
      
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch(SQLException e) {
            System.out.println("way way"+e);
            //results = e.toString();
        }
    }
        
    public boolean checkUser(String username, String password) {
        boolean bool = false;
        try  {
            select("select username from users where username = '" + username + "' and password = '" + password + "'");
            if(rs.next()) {
                bool = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bool;
    }
    
    public boolean createAttendanceRecord(String module, String sessionReference) {
        PreparedStatement ps = null;
        boolean success = false;
        try {
            ps = connection.prepareStatement("INSERT INTO ATTENDANCE_RECORD VALUES (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, String.valueOf(retrieveNextAttendanceRecordID()));
            ps.setString(2, generateAttendanceRecordReference());
            ps.setString(3, module);
            ps.setString(4, getSessionIDFromReference(sessionReference));
            
            success = ps.executeUpdate() != 0;
            ps.close();
            System.out.println("1 attendance record added.");
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    
    public String getLatestAttendanceRecordID() {
        String query = "SELECT ID FROM ATTENDANCE_RECORD ORDER BY ID DESC FETCH FIRST 1 ROWS ONLY";
        String[] latestID = retrieveQueryWithStringArray(query);
        
        return latestID[0];
    }
    
    public void generateAttendeeRecords(List<String> attendees, String attendanceRecordID){
        PreparedStatement ps = null;
        try {
            for (String attendee : attendees) {
                ps = connection.prepareStatement("INSERT INTO ATTENDEE_RECORD VALUES (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, String.valueOf(retrieveNextAttendeeRecordID())); 
                ps.setString(2, attendee);            
                ps.setString(3, attendanceRecordID);
                ps.executeUpdate();
            }
        
            ps.close();
            System.out.println("1 session added.");
        } catch (SQLException ex) {
            Logger.getLogger(Jdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getSessionIDFromReference(String sessionReference) { 
        String query = "SELECT ID FROM SESSION WHERE REFERENCE = '" + sessionReference + "'";
        String[] sessionID = retrieveQueryWithStringArray(query);
        
        return sessionID[0];
    }
    
    public String getAttendanceRecordIDFromReference(String attendanceRecordReference) { 
        String query = "SELECT ID FROM ATTENDANCE_RECORD WHERE REFERENCE = '" + attendanceRecordReference + "'";
        String[] sessionID = retrieveQueryWithStringArray(query);
        
        return sessionID[0];
    }
    
    public String getAttendanceRecordsForSession(String sessionID, HttpSession session) throws SQLException {
        String query = "SELECT REFERENCE FROM ATTENDANCE_RECORD WHERE SESSION_ID = " + sessionID;
        String attendanceRecords = retrieve(query, session);
        
        return attendanceRecords;
    }
    
    public String getAttendeesForAttendanceRecord(String attendanceRecordID, HttpSession session) throws SQLException {
        String query = "SELECT USERS.NAME, USERS.STUDENTNUMBER FROM ATTENDEE_RECORD " +
                       "JOIN USERS ON USERS.STUDENTNUMBER = ATTENDEE_RECORD.STUDENTNUMBER " + 
                       "WHERE ATTENDEE_RECORD.ATTENDANCERECORD_ID = '" + attendanceRecordID + "'";
        String attendanceRecords = retrieve(query, session);
        
        return attendanceRecords;
    }
    
    public void closeAll(){
        try {
            rs.close();
            statement.close(); 		
            //connection.close();                                         
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }
    
    public static void main(String[] args) throws SQLException {
        String str = "select * from users";
        String insert = "INSERT INTO `Users` (`username`, `password`) VALUES ('meaydin', 'meaydin')";
        String update = "UPDATE `Users` SET `password`='eaydin' WHERE `username`='meaydin' ";
        String db = "MyDB";
        
        Jdbc jdbc = new Jdbc(str);
        Connection conn = null;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/database/", "test", "test");
        }
        catch(ClassNotFoundException | SQLException e){
            
        }
        jdbc.connect(conn);
        String [] users = {"eaydin","benim","benim"};
        if (!jdbc.exists(users[0]))
            jdbc.insertUser(users);            
        else {
                jdbc.update(users);
                System.out.println("user name exists, change to another");
        }
        jdbc.delete("aydinme");
        
        jdbc.closeAll();
    }            
}
