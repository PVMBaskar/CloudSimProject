/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCOM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author m_bas
 */
public class CreateDataFile {

    static String driver = "com.mysql.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/";
    static String username = "root";
    static String password = "";
    static String dbname = "DCOM";

    public static void main(String args[]) {
        Connection con = null;
        Statement st = null;

        try {
            Class.forName(driver);
            System.out.println("Connecting to DataBase");
            con = (Connection) DriverManager.getConnection(url, username, password);
            System.out.println("Creating DataBase");
            st = (Statement) con.createStatement();
            String sql = "create database " + dbname;
            st.executeUpdate(sql);
            System.out.println("DataBase  Created....");
            System.out.println("*********************");
            String sql1 = "use " + dbname;
            st.executeUpdate(sql1);
            st.executeUpdate("create table users(loginname varchar(50), password varchar(50), rights varchar(50))");
            st.executeUpdate("create table DCOM(NumberOfWorkloads int, Workloads varchar(50), Latency double, Energy double, ExecutionTime double)");
            st.executeUpdate("create table COM(NumberOfWorkloads int, Workloads varchar(50), Latency double, Energy double, ExecutionTime double)");

            st.close();
            con.close();
            JOptionPane.showMessageDialog(null, "The  Database File and tables created Succussfully!!");

        } catch (Exception e) {
            System.out.println("error on databasee creation :" + e.getMessage());
        }

    }
}
