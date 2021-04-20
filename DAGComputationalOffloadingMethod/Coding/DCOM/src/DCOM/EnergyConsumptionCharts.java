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
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author m_bas
 */
public class EnergyConsumptionCharts extends ApplicationFrame {

    String workload, workload1;
    int wf, wf1;
    double energy, energy1;

    public EnergyConsumptionCharts(String applicationTitle, String chartTitle) {
        super(applicationTitle);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "Number of Tasks",
                "Energy consumed(in kwh)",
                createDataset(),
                PlotOrientation.VERTICAL, true, true, false);

        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        final XYPlot plot = xylineChart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setSeriesPaint(1, Color.RED);

        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setOutlinePaint(Color.BLUE);
        plot.setOutlineStroke(new BasicStroke(2.0f));
        plot.setBackgroundPaint(Color.DARK_GRAY);
        plot.setRangeGridlinesVisible(true);
        setContentPane(chartPanel);
    }

    private XYDataset createDataset() {
        final XYSeriesCollection dataset = new XYSeriesCollection();

        final XYSeries firefox = new XYSeries("COM");
        final XYSeries firefox1 = new XYSeries("DCOM");

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
            System.out.println("Got DB Connection");

//            String str = "select avg(NumberOfFogManager+NumberOfFogCell+NumberOfFogNodes+NumberOfIOT+2),avg(EnergyConsumed) From Latencygade Group By NumberOfFogManager,NumberOfFogCell,NumberOfFogNodes,NumberOfPatients";
            String str = "select NumberOfWorkloads,avg(Energy) From COM Group By NumberOfWorkloads";
            ResultSet rs = stmt.executeQuery(str);

            if (rs != null) {
                while (rs.next()) {
//                    workload = rs.getString(1);
                    wf = rs.getInt(1);
//                    wf = Integer.valueOf(workload.substring(workload.indexOf("_")+1).toString());
                    energy = rs.getDouble(2);
                    firefox.add(wf, energy);
                }
            }

            dataset.addSeries(firefox);

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

//            String str1 = "select avg(NumberOfFogManager+NumberOfFogCell+NumberOfFogNodes+NumberOfIOT+2),avg(EnergyConsumed) From Latencypso Group By NumberOfFogManager,NumberOfFogCell,NumberOfFogNodes,NumberOfPatients";
//            String str1 = "select Workload,avg(EnergyConsumed) From Latencypso Group By Workload";
            String str1 = "select NumberOfWorkloads,avg(Energy) From DCOM Group By NumberOfWorkloads";

            ResultSet rs1 = stmt1.executeQuery(str1);

            if (rs1 != null) {
                while (rs1.next()) {
//                    workload1 = rs1.getString(1);
                    wf1 = rs1.getInt(1);
//                    wf1 = Integer.valueOf(workload1.substring(workload1.indexOf("_") + 1).toString());
                    energy1 = rs1.getDouble(2);
                    firefox1.add(wf1, energy1);
                }
            }

            dataset.addSeries(firefox1);

            stmt1.close();
            conn1.close();

        } catch (Exception e) {

        }
        return dataset;
    }

    public static void main(String args[]) {
        EnergyConsumptionCharts energyConsumptionChart = new EnergyConsumptionCharts("Power", "Energy Consumption");
        energyConsumptionChart.pack();
        RefineryUtilities.centerFrameOnScreen(energyConsumptionChart);
        energyConsumptionChart.setVisible(true);
    }

}
