package ru.otus.gromov.connection;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

class ConnectionHelper {

    static Connection getConnection() {
        try {
            //Driver driver = new com.mysql.cj.jdbc.Driver();
            Driver driver = (Driver) Class.forName("org.h2.Driver").getConstructor().newInstance();
            DriverManager.registerDriver(driver);

            String url = "jdbc:h2:" +       //db type
                    "mem:" +               //host name
                    //"3306/" +                    //port
                    "test?" +               //db name
                    "user=sa&" +              //login
                    "password=&" +          //password
                    "useSSL=false";              //do not use Secure Sockets Layer


            return DriverManager.getConnection(url);
        } catch (SQLException |
                InstantiationException |
                InvocationTargetException |
                NoSuchMethodException |
                IllegalAccessException |
                ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
