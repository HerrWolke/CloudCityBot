package de.mrcloud.SQL;

import de.mrcloud.utils.JDAUtils;
import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class SqlMain {
    public static Connection mariaDB() {
        Connection conn = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");


            conn = DriverManager.getConnection(Static.DB_CONNECT_URL_PC, "root", Static.DB_PW);
            return conn;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Error while connecting to DB");

            System.exit(0);
            return null;
        }
    }

    public static void main(String[] args) {
        {
            mariaDB();
        }
    }

    public void registerUser(Member member) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        try {
            Statement statment = mariaDB().createStatement();

            statment.executeQuery("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
