/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCOM;

import java.awt.BasicStroke;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author m_bas
 */
public class ExecutionTimeChart extends ApplicationFrame{
    String[] workload, workload1;
    double[] exeTime, exeTime1;

    public ExecutionTimeChart(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Workloads",
                "Execution Time(s)",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(450, 300));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {
        String[] Algorithm = new String[2];
        Algorithm[0] = "COM";
        Algorithm[1] = "DCOM";
        

        final DefaultCategoryDataset dataset
                = new DefaultCategoryDataset();

        try {

            Connection conn = null;
            String url = "jdbc:mysql://localhost:3306/";
            String dbName = "DCOM";
            String driver = "com.mysql.jdbc.Driver";
            String userName = "root";
            String dbpassword = "";
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, dbpassword);
            Statement stmt = conn.createStatement();

            /**
             * data retrieved to draw a graph
             */
//            String str = "SELECT Workload,avg(Latency) FROM Latencygade GROUP BY Workload";
            String str = "SELECT Workloads,avg(ExecutionTime) FROM COM GROUP BY NumberOfWorkloads";

            String xCount = "SELECT COUNT(DISTINCT NumberOfWorkloads) FROM COM ORDER BY NumberOfWorkloads";

            ResultSet resultSet = stmt.executeQuery(xCount);
            int z = 0;
            if (resultSet != null) {
                while (resultSet.next()) {
                    z = resultSet.getInt(1);
                }
            }
            System.out.println("z :" + z);
            workload = new String[z];
            exeTime = new double[z];

            ResultSet dataResultSet = stmt.executeQuery(str);

            int k = 0;
            if (dataResultSet != null) {
                while (dataResultSet.next()) {
                    workload[k] = (dataResultSet.getString(1));
                    exeTime[k] = dataResultSet.getDouble(2);
                    k++;
                }
            }
            stmt.close();
            conn.close();

            Connection conn1 = null;
            String url1 = "jdbc:mysql://localhost:3306/";
            String dbName1 = "DCOM";
            String driver1 = "com.mysql.jdbc.Driver";
            String userName1 = "root";
            String dbpassword1 = "";
            Class.forName(driver1).newInstance();
            conn1 = DriverManager.getConnection(url1 + dbName1, userName1, dbpassword1);
            Statement stmt1 = conn1.createStatement();

            /**
             * data from PSO algorithm
             */
//            String str1 = "SELECT avg(NumberOfFogManager+NumberOfFogCell+NumberOfFogNodes+NumberOfIOT+2),avg(Latency) FROM LatencyPSO GROUP BY NumberOfFogManager,NumberOfFogCell,NumberOfFogNodes,NumberOfPatients";
//            String str1 = "SELECT Workload,avg(Latency) FROM LatencyPSO GROUP BY Workload";
//            String xCount1 = "SELECT COUNT(DISTINCT Workload) FROM LatencyPSO ORDER BY Workload";
            String str1 = "SELECT Workloads,avg(ExecutionTime) FROM DCOM GROUP BY NumberOfWorkloads";

            String xCount1 = "SELECT COUNT(DISTINCT NumberOfWorkloads) FROM DCOM ORDER BY NumberOfWorkloads";

            ResultSet resultSetCount1 = stmt1.executeQuery(xCount1);
            int y = 0;
            if (resultSetCount1 != null) {
                while (resultSetCount1.next()) {
                    y = resultSetCount1.getInt(1);
                }
            }
            workload1 = new String[y];
            exeTime1 = new double[y];

            ResultSet PSOResultSet = stmt1.executeQuery(str1);

            int j = 0;
            if (PSOResultSet != null) {
                while (PSOResultSet.next()) {
                    workload1[j] = (PSOResultSet.getString(1));
//                    workload1[j] = workload1[j].substring(0, workload1[j].indexOf("."));
                    exeTime1[j] = PSOResultSet.getDouble(2);

                    j++;
                }
            }

            stmt1.close();
            conn1.close();


            /* COM    */
            for (int a = 0; a < z; a++) {
                dataset.addValue(exeTime[a], Algorithm[0], workload[a]);
            }

            /* DCOM    */
            for (int b = 0; b < y; b++) {
                dataset.addValue(exeTime1[b], Algorithm[1], workload1[b]);
            }

        } catch (Exception e) {
            System.out.println("database access Error " + e);
        }
        return dataset;
    }

    public static void main(String[] args) {
        ExecutionTimeChart chart = new ExecutionTimeChart("Application",
                "Execution Time");
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}
