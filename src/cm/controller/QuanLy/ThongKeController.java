package cm.controller.QuanLy;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import cm.ConnectToDatabase;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DateTimeStringConverter;

/**
 * FXML Controller class
 *
 * @author XyzC
 */
public class ThongKeController implements Initializable {
    @FXML
    AnchorPane lab1;
    @FXML
    AnchorPane lab2;
    @FXML
    AnchorPane lab3;
    ConnectToDatabase con;
    PreparedStatement ps;
    ResultSet rs;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
   try {
        String sql1 = "SELECT SUM(Chi_Phi_Dich_Vu) as DV,DATE(Thoi_gian_kham) as DT FROM Thanh_Toan natural join Phien_Kham GROUP BY DATE(Thoi_gian_kham);";
        String title1 = "Thống kê doanh thu dịch vụ";
        String sql2 = "SELECT SUM(Chi_Phi_Thuoc) as DV,DATE(Thoi_gian_kham) as DT FROM Thanh_Toan natural join Phien_Kham GROUP BY DATE(Thoi_gian_kham);";
        String title2 = "Thống kê doanh thu thuốc";
        String sql3 = "SELECT SUM(Chi_Phi) as DV,DATE(Thoi_gian_kham) as DT FROM Thanh_Toan natural join Phien_Kham GROUP BY DATE(Thoi_gian_kham);";
        String title3 = "Thống kê doanh thu dịch vụ";
        chartinit(sql1,title1,lab1);
        chartinit(sql2, title2, lab2);
        chartinit(sql3, title3, lab3);
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ThongKeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 
    
    private void chartinit(String sql,String title,AnchorPane lab) throws SQLException, ParseException{
        
            DateAxis xAxis = new DateAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Thời gian");  
            yAxis.setLabel("VNĐ");
          
            LineChart<Date,Number> chart = new LineChart<>(xAxis,yAxis);
            chart.setPrefSize(1075, 400);
            chart.setTitle(title);
            con = new ConnectToDatabase();
            ObservableList<XYChart.Series<Date,Number>> chartData = getData(sql);
            chart.setData(chartData);
            lab.getChildren().add(chart);
            this.addSliceTooltip(chart);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private  ObservableList<XYChart.Series<Date, Number>> getData(String sql) throws SQLException, ParseException{
        
        XYChart.Series<Date, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");
        
        DateTimeStringConverter format = new DateTimeStringConverter(Locale.FRANCE,"yyyy-MM-dd"); 
        ps=con.getPS(sql);
        rs = ps.executeQuery();
        
        while(rs.next())
        {
            series.getData().add(new XYChart.Data(format.fromString(rs.getString("DT")),rs.getFloat("DV")));
        }
        ps.close();
        rs.close();
        ObservableList<XYChart.Series<Date, Number>> data =
                FXCollections.<XYChart.Series<Date, Number>>observableArrayList();
        data.add(series);
        return data;
    }
    private void addSliceTooltip(LineChart<Date,Number> chart) {
        
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        for(XYChart.Series<Date,Number> s : chart.getData())
        {
            for (XYChart.Data<Date,Number> d : s.getData()) {
                Node sliceNode = d.getNode();
// Create and install a Tooltip for the slice
            String msg ="Ngày:"+ formatter.format(d.getXValue())+"\n"+"Doanh thu: "+d.getYValue();
            Tooltip tt = new Tooltip(msg);
            tt.setStyle("-fx-background-color: yellow;" +
            "-fx-text-fill: black;");
            Tooltip.install(sliceNode, tt);
            sliceNode.setOnMouseEntered(e -> {
            });
        }
        }
    }
}
