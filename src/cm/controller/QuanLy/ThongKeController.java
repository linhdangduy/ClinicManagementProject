package cm.controller.QuanLy;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import cm.ConnectToServer;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DateTimeStringConverter;
import javax.sql.rowset.CachedRowSet;

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
    @FXML
    ComboBox<String> cbChart1;
    @FXML
    ComboBox<String> cbChart2;
    @FXML
    ComboBox<String> cbChart3;
    ConnectToServer con;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
   try {
        String sql1 = "SELECT SUM(Tong_Phi_Dich_Vu) as DV,DATE(Thoi_gian_kham) as DT FROM Thanh_Toan natural join Phien_Kham GROUP BY DATE(Thoi_gian_kham);";
        String title1 = "Thống kê doanh thu dịch vụ";
        String sql2 = "SELECT SUM(Tong_Phi_Thuoc) as DV,DATE(Thoi_gian_kham) as DT FROM Thanh_Toan natural join Phien_Kham GROUP BY DATE(Thoi_gian_kham);";
        String title2 = "Thống kê doanh thu thuốc";
        String sql3 = "SELECT SUM(Chi_Phi) as DV,DATE(Thoi_gian_kham) as DT FROM Thanh_Toan natural join Phien_Kham GROUP BY DATE(Thoi_gian_kham);";
        String title3 = "Thống kê tổng doanh thu";
        String sql1m = "SELECT SUM(Tong_Phi_Dich_Vu) as DV,DATE_FORMAT(Thoi_gian_kham,'%Y-%m') as DT FROM Thanh_Toan natural join Phien_Kham GROUP BY DATE_FORMAT( Thoi_Gian_Kham,'%Y-%m') ;";
        String sql2m = "SELECT SUM(Tong_Phi_Thuoc) as DV,DATE_FORMAT(Thoi_gian_kham,'%Y-%m') as DT FROM Thanh_Toan natural join Phien_Kham GROUP BY DATE_FORMAT( Thoi_Gian_Kham,'%Y-%m') ;";
        String sql3m = "SELECT SUM(Chi_Phi) as DV,DATE_FORMAT(Thoi_gian_kham,'%Y-%m') as DT FROM Thanh_Toan natural join Phien_Kham GROUP BY DATE_FORMAT( Thoi_Gian_Kham,'%Y-%m') ;";
        
        initcb(cbChart1);
        initcb(cbChart2);
        initcb(cbChart3);

        chartinit(sql1,title1,lab1,0);
        chartinit(sql2,title2,lab2,0);
        chartinit(sql3,title3,lab3,0);
        
        initlisten(cbChart1, lab1, sql1,sql1m, title1);
        initlisten(cbChart2, lab2, sql2,sql2m, title1);
        initlisten(cbChart3, lab3, sql3,sql3m, title1);

     
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(ThongKeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 
    private void initcb(ComboBox<String> cb){
        cb.getItems().addAll("Ngày","Tháng");
        cb.setValue("Ngày");
        
    }
    
    private void initlisten(ComboBox cb,AnchorPane lab,String sql1,String sql2, String title){
        cb.valueProperty().addListener((observable, oldValue , newValue) -> {
            switch(newValue.toString())
                {
                    case "Ngày" :
                    {
                        lab.getChildren().remove(0);
                        try {
                        chartinit(sql1, title, lab, 0);
                        } catch (ParseException | SQLException ex) {
                        Logger.getLogger(ThongKeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                        break;
                    case "Tháng" :
                    {
                        lab.getChildren().remove(0);
                        try {
                        chartinit(sql2, title, lab, 1);
                        } catch (ParseException | SQLException ex) {
                        Logger.getLogger(ThongKeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            }
        });
    }
    private void chartinit(String sql,String title,AnchorPane lab,int x) throws ParseException, SQLException{
            //x=0 theo date
            //x!=0 theo month
            DateAxis xAxis = new DateAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Thời gian");  
            yAxis.setLabel("VNĐ");
          
            LineChart<Date,Number> chart = new LineChart<>(xAxis,yAxis);
            chart.setPrefSize(1075, 400);
            chart.setTitle(title);
            ObservableList<XYChart.Series<Date,Number>> chartData = getData(sql,x);
            chart.setData(chartData);
            lab.getChildren().add(chart);
            this.addSliceTooltip(chart,x);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private  ObservableList<XYChart.Series<Date, Number>> getData(String sql,int x) throws SQLException, ParseException{
        //x=0 theo date
        //x!=0 theo month
        if(x == 0)
        {
            XYChart.Series<Date, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu");

            DateTimeStringConverter format = new DateTimeStringConverter(Locale.CHINA,"yyyy-MM-dd"); 
            con = new ConnectToServer();
            con.sendToServer(sql);
            Object ob = con.receiveFromServer();
            if (ob.getClass().toString().equals("class java.lang.String")) {
            }
            else{
                CachedRowSet rs = (CachedRowSet)ob;
                while(rs.next())
                {
                    series.getData().add(new XYChart.Data(format.fromString(rs.getString("DT")),rs.getFloat("DV")));
                }
                con.sendToServer("done");
            }
            ObservableList<XYChart.Series<Date, Number>> data =
                    FXCollections.<XYChart.Series<Date, Number>>observableArrayList();
            data.add(series);
            return data;
        }
        else
        {
            XYChart.Series<Date, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu");

            DateTimeStringConverter format = new DateTimeStringConverter(Locale.CHINA,"yyyy-MM"); 
            con = new ConnectToServer();
            con.sendToServer(sql);
            Object ob = con.receiveFromServer();
            if (ob.getClass().toString().equals("class java.lang.String")) {
            }
            else{
                CachedRowSet rs = (CachedRowSet)ob;
                while(rs.next())
                {
                    series.getData().add(new XYChart.Data(format.fromString(rs.getString("DT")),rs.getFloat("DV")));
                }
                con.sendToServer("done");
            }
            ObservableList<XYChart.Series<Date, Number>> data =
                    FXCollections.<XYChart.Series<Date, Number>>observableArrayList();
            data.add(series);
            return data;
        }
    }
    private void addSliceTooltip(LineChart<Date,Number> chart,int x) {
        //x=0 theo date
        //x!=0 theo month
        if(x!=0)
        {
            Format formatter = new SimpleDateFormat("MM-yyyy");
            for(XYChart.Series<Date,Number> s : chart.getData())
            {
                for (XYChart.Data<Date,Number> d : s.getData()) {
                    Node sliceNode = d.getNode();
    // Create and install a Tooltip for the slice
                String msg ="Tháng:"+ formatter.format(d.getXValue())+"\n"+"Doanh thu: "+d.getYValue()+" VNĐ";
                Tooltip tt = new Tooltip(msg);
                tt.setStyle("-fx-background-color: yellow;" +
                "-fx-text-fill: black;");
                Tooltip.install(sliceNode, tt);
                sliceNode.setOnMouseEntered(e -> {
                });
            }
            }
        }
        else{
            Format formatter = new SimpleDateFormat("dd-MM-yyyy");
            for(XYChart.Series<Date,Number> s : chart.getData())
            {
                for (XYChart.Data<Date,Number> d : s.getData()) {
                    Node sliceNode = d.getNode();
    // Create and install a Tooltip for the slice
                String msg ="Ngày:"+ formatter.format(d.getXValue())+"\n"+"Doanh thu: "+d.getYValue()+" VNĐ";
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
}
