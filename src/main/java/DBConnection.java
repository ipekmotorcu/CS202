import java.sql.*;
public class DBConnection {
    final static String url = "jdbc:mysql://localhost:3306/proje"; //projenin adı.
    final static String user = "root"; //username and password of mysql
    final static String password = "12341234";


    public static Connection getConnection(){
        Connection myConn = null;
        try{
            myConn = DriverManager.getConnection(url, user, password);
        } catch (Exception e){
            System.out.println("\n>>> ERROR! The Program Can NOT Connect to the Database. <<<");
        }
        return myConn; //so this returns a Connection object.
    }
}
