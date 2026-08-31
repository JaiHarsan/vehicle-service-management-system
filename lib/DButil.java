import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil{
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/emp_db","root","0512");
        System.out.print("Connected....");
    }
}
