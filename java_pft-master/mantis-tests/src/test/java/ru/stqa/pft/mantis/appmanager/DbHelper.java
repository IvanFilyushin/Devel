package ru.stqa.pft.mantis.appmanager;

import org.apache.commons.net.ftp.FTPClient;
import ru.stqa.pft.mantis.models.UserData;

import java.sql.*;

/**
 * Created by DELL on 07.09.16.
 */
public class DbHelper {

    private final ApplicationManager app;

    public DbHelper(ApplicationManager app) {
        this.app = app;
    }

    public UserData getUser() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/bugtracker?serverTimezone=UTC&user=root&password=");
            Statement st = conn.createStatement();
            // выбираем из тестеров и программистов
            ResultSet rs = st.executeQuery("select username, email from mantis_user_table" +
                    " where access_level=25 or access_level=55");
            rs.first();
            UserData user = new UserData().withUsername(rs.getString("username")).withEmail(rs.getString("email"));

            rs.close();
            st.close();
            conn.close();

            return user;

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }


        return null;
    }


}
