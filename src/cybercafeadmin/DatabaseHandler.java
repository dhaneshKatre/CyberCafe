package cybercafeadmin;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler {
    private static Connection con;
    private static final String ALPHA_NUMERIC_STRING = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
    private static final String url = "jdbc:sqlserver://localhost:1433;databaseName=cyber;integratedSecurity=true";
    public DatabaseHandler(){ 
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
            con = DriverManager.getConnection(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static ArrayList<String> getStatus(){
        ArrayList<String> info = new ArrayList<>();
        try {
            PreparedStatement st = con.prepareStatement("select pcs.id, pcs.isAvailable, users.uName, pcs.inTime from pcs left join users on pcs.currentUser = users.id");
            ResultSet rt = st.executeQuery();
            while(rt.next()){
                info.add(rt.getString(1)+"\t"+rt.getString(2)+"\t"+rt.getString(3)+"\t\t"+rt.getString(4)+"\n");
            }
            return info;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<String> getReport(){
        ArrayList<String> info = new ArrayList<>();
        try {
            PreparedStatement st = con.prepareStatement("select report.id, report.today, report.bill, users.uName from report left join users on report.userId = users.id");
            ResultSet rt = st.executeQuery();
            while(rt.next()){
                System.out.println(rt.getString(2));
                info.add(rt.getString(1)+"  "+rt.getString(2).substring(0, rt.getString(2).indexOf(" "))+"\t"+rt.getString(3)+"   "+rt.getString(4)+"\n");
            }
            return info;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
   
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
    
    public static String initSession(String id) {
        try {
            PreparedStatement stat = con.prepareStatement("select * from pcs where isAvailable = 'Y'");
            ResultSet rt = stat.executeQuery();
            if(rt.next()){
                PreparedStatement st = con.prepareStatement("update pcs set isAvailable = 'N', currentUser = ?, inTime = CURRENT_TIMESTAMP where id = ?");
                st.setString(1, id);
                st.setString(2, rt.getString(1));
                PreparedStatement sr = con.prepareStatement("update users set using = ? where id = ?");
                sr.setString(1, rt.getString(1));
                sr.setInt(2, Integer.parseInt(id));
                st.execute();
                sr.execute();
                return rt.getString(1);
            } else return null;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setBill(String id, int bill) {
        try {
            PreparedStatement st = con.prepareStatement("insert into report(userId, bill, today) values(?,?,CURRENT_TIMESTAMP)");
            st.setInt(1, Integer.parseInt(id));
            st.setInt(2, bill);
            st.execute();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public static ArrayList<String> terminateSession(String id) {
        ArrayList<String> info = new ArrayList<>();
        try {
            String inTime = null;
            PreparedStatement stat = con.prepareStatement("select using from users where id = ?");
            stat.setInt(1, Integer.parseInt(id));
            ResultSet rt = stat.executeQuery();
            if(rt.next()){
                PreparedStatement op = con.prepareStatement("select inTime from pcs where id = ?");
                op.setString(1, rt.getString(1));
                ResultSet wt = op.executeQuery();
                if(wt.next()) inTime = wt.getString(1);
                PreparedStatement st = con.prepareStatement("update pcs set isAvailable = 'Y', currentUser = NULL, inTime = NULL where id = ?");
                st.setString(1, rt.getString(1));
                st.execute();
                PreparedStatement sr = con.prepareStatement("update users set using = NULL where id = ?");
                sr.setInt(1, Integer.parseInt(id));
                sr.execute();
                info.add(inTime); 
                info.add(id);
            } else {
                System.err.println("FATAL ERROR! System Inconsistent!");
                System.exit(0);
            }
            return info;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static HashMap<String, String> getInfo(String id){
        HashMap<String, String> info = new HashMap<>();
        try {
            PreparedStatement stat = con.prepareStatement("select * from users where id = ?");
            stat.setInt(1, Integer.parseInt(id));
            ResultSet rt = stat.executeQuery();
            if(rt.next()){
                info.put("name", rt.getString(2));
                info.put("email", rt.getString(3));
                info.put("phone", rt.getString(4));
                info.put("address", rt.getString(5));
            }
        } catch(SQLException r){
            r.printStackTrace();
        }
        return info;
    }
    
    public static boolean updateInfo(HashMap<String, String> info){
        try{
            PreparedStatement stat = con.prepareStatement("update users set uName = ?, email = ?, phone = ?, address = ? where id = ?");
            stat.setString(1, info.get("name"));
            stat.setString(2, info.get("email"));
            stat.setString(3, info.get("phone"));
            stat.setString(4, info.get("address"));
            stat.setInt(5, Integer.parseInt(info.get("id")));
            stat.execute();
            return true;
        } catch(SQLException r){
            r.printStackTrace();
            return false;
        }
    } 
    
    public static ArrayList<String> getAllCurrentUserIds(){
        ArrayList<String> ids = new ArrayList<>();
        try {
            PreparedStatement stat = con.prepareStatement("select id from users where users.using is not null order by id ");
            ResultSet rt = stat.executeQuery();
            while(rt.next()){
                ids.add(rt.getString(1));
            }
        } catch(SQLException r){
            r.printStackTrace();
        }
        return ids;
    }
    
    
    public static ArrayList<String> getAllAvailableIds(){
        ArrayList<String> ids = new ArrayList<>();
        try {
            PreparedStatement stat = con.prepareStatement("select id from users where users.using is null order by id ");
            ResultSet rt = stat.executeQuery();
            while(rt.next()){
                ids.add(rt.getString(1));
            }
        } catch(SQLException r){
            r.printStackTrace();
        }
        return ids;
    }
    
    public static ArrayList<String> getAllIds(){
        ArrayList<String> ids = new ArrayList<>();
        try {
            PreparedStatement stat = con.prepareStatement("select id from users order by id");
            ResultSet rt = stat.executeQuery();
            while(rt.next()){
                ids.add(rt.getString(1));
            }
        } catch(SQLException r){
            r.printStackTrace();
        }
        return ids;
    }
    
    public static String addNewUser(String email, String name, String phone, String address) {
        try {
            PreparedStatement stat = con.prepareStatement("insert into users(uName, email, phone, address) values (?, ?, ?, ?)");
            stat.setString(1, name);
            stat.setString(2, email);
            stat.setString(3, phone);
            stat.setString(4, address);
            stat.execute();
            PreparedStatement st = con.prepareStatement("select id from users where email = ?");
            st.setString(1, email);
            ResultSet rt = st.executeQuery();
            if(rt.next()){
                return rt.getString(1);
            } else return null;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void closeConnection() {
        try {
            con.close();
            Runtime.getRuntime().halt(0);
        } catch(Exception r){
            r.printStackTrace();
        }
    }
    
    public static boolean loginAdmin(String email, String password){
        try {
            PreparedStatement stat = con.prepareStatement("select * from cybermin where email = ? and uPassword = ?");
            stat.setString(1, email);
            stat.setString(2, password);
            ResultSet rt = stat.executeQuery();
            return rt.next();
        } catch (Exception t){
            t.printStackTrace();
            return false;
        }
    }
}
