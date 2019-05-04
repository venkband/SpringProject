package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class MyApplication {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("driver found");
            Connection con = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/loginusers", "root", "root");
            System.out.println("connection success");

        } catch (ClassNotFoundException e) {
            System.err.println("class not found");
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.err.println("Connection unsuccessful");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
