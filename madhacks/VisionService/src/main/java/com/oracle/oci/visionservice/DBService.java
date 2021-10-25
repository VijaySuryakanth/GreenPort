package com.oracle.oci.visionservice;

import com.oracle.oci.visionservice.model.Containers;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author VKOKATNU
 */
public class DBService {

    // The recommended format of a connection URL is the long format with the
    // connection descriptor.
    //final static String DB_URL = "jdbc:oracle:thin:@myhost:1521/myorcldbservicename";
    // For ATP and ADW - use the TNS Alias name along with the TNS_ADMIN when using 18.3 JDBC driver
    // final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=/Users/test/wallet_dbname";
    // In case of windows, use the following URL 
    //final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=C:/Users/test/wallet_dbname";
    //static String DB_URL="jdbc:oracle:thin:@gpadw_low?TNS_ADMIN=C:/lift/SRs/MadHacks/samples/Wallet_gpadw";
    static String DB_URL = "jdbc:oracle:thin:@gpadw_low?TNS_ADMIN=";
    final static String DB_USER = "admin";
    final static String DB_PASSWORD = "32Welcome@54";


    /**
     * 
     * @throws SQLException 
     */
    public void service() throws SQLException {
        saveData(null);
    }

    public Connection getConnection() {
        OracleConnection connection = null;
        try {
            Path path = Paths.get("src/main/resources/Wallet_gpadw").toAbsolutePath();
            DB_URL = DB_URL.concat(path.toString().replace("\\", "/"));

            //DB_URL = "jdbc:oracle:thin:@gpadw_low?TNS_ADMIN=/C:/lift/SRs/practice/fn/ws/madhacks/VisionService/src/main/resources/Wallet_gpadw";
            //DB_URL = "jdbc:oracle:thin:@gpadw_low?TNS_ADMIN="+Paths.get("src/main/resources/Wallet_gpadw").toUri();
            System.out.println(" -- DB_URL -- " + DB_URL);
            Properties info = new Properties();
            info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
            info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
            info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");

            OracleDataSource ods = new OracleDataSource();
            ods.setURL(DB_URL);
            ods.setConnectionProperties(info);
            System.err.println(" -- Got the connection -- ");
            // With AutoCloseable, the connection is closed automatically.

            connection = (OracleConnection) ods.getConnection();
            // Get the JDBC driver name and version 
            DatabaseMetaData dbmd = connection.getMetaData();
            System.out.println("Driver Name: " + dbmd.getDriverName());
            System.out.println("Driver Version: " + dbmd.getDriverVersion());
            // Print some connection properties
            System.out.println("Default Row Prefetch Value is: "+ connection.getDefaultRowPrefetch());
            System.out.println("Database Username is: " + connection.getUserName());
            System.out.println();
            // Perform a database operation 
            //printResults(connection);
        } catch (Exception ex) {
            Logger.getLogger(DBService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    /**
     * 
     * @param connection
     * @param container 
     */
    public void saveData(Containers container){
        System.out.println(" -- SaveData -- ");
        Connection connection = getConnection();
        String query = "INSERT INTO CONTAINERS VALUES('"+container.getContainerName()+"', '"+container.getContainerId()+"')";        
        System.out.println(" -- query -- "+query);
        
        try ( Statement statement = connection.createStatement()) {            
            int resuslt = statement.executeUpdate(query);
            System.out.println(" -- SaveData -- resuslt -- "+resuslt);
        } catch (SQLException ex) {
            Logger.getLogger(DBService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     * Displays name and ID from the CONTAINERS table.
     */
    public static void printResults(Connection connection) throws SQLException {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        try ( Statement statement = connection.createStatement()) {
            try ( ResultSet resultSet = statement
                    .executeQuery("select * from CONTAINERS")) {
                System.out.println("NAME" + "  " + "ID");
                System.out.println("---------------------");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1) + " "
                            + resultSet.getString(2) + " ");
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {
        System.out.println("processing ... " + Paths.get("src/main/resources/Wallet_gpadw").toAbsolutePath());
        new DBService().service();
        //Path path = Paths.get("src/main/resources/Wallet_gpadw").toAbsolutePath();
        //path.toUri();
        //System.out.println(" -- -- "+Paths.get("src/main/resources/Wallet_gpadw").toUri().getPath());
        //System.out.println(" -- -- "+Paths.get("src/main/resources/Wallet_gpadw").toAbsolutePath().toString().replace("\\", "/"));
    }
}
