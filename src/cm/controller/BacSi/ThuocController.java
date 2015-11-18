/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

import cm.ConnectToDatabase;
import cm.model.Thuoc;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static javax.management.remote.JMXConnectorFactory.connect;
import javax.swing.plaf.nimbus.State;

/**
 *
 * @author vincent
 */
public class ThuocController implements Initializable, PaneInterface  {

    @FXML
    private Label lblTenThuoc;
    @FXML
    private Label lblDonVi;
    @FXML
    private Label lblGiaThuoc;
    @FXML
    private Label lblTrangThai;
    @FXML
    private TableView<Thuoc> ThuocTable;
    @FXML
    private TableColumn ColThuocMa;
    @FXML
    private TableColumn ColThuocTenthuoc;
    @FXML
    private TableColumn ColThuocGiathuoc;
    @FXML
    private TableColumn ColThuocTrangthai;
    @FXML
    private TableColumn ColThuocCongdung;
    @FXML 
    private TableColumn ColThuocDonvi;
    @FXML
    private TextArea taCongdung;
    @FXML
    private ObservableList<Thuoc> ThuocData=FXCollections.observableArrayList(); 
    
    private String Gia;
    ConnectToDatabase con;
    private Statement st;
    private ResultSet rs;
    private PreparedStatement ps;
   
    @FXML
    private BacSiController parentPane;
    @FXML
    private ComboBox <String> cbLoc;
    @FXML
    private TextField tfLoc;
    @FXML
    private VBox paneThemThuoc;
   
    //=======================================================================================

    @FXML
    private void handleBtnTroLai(ActionEvent event) {
        parentPane.setPane("tiepnhan");
    }

    @FXML
    private void handleBtnThem(ActionEvent event) {

    }
    
    @FXML
    private void handleBtnXong(ActionEvent event) throws SQLException {
      
    }
   
    public void addThuocData() throws SQLException
    {
        String sql ="SELECT * FROM Thuoc ORDER BY Ma_Thuoc ASC";
        rs = con.getRS(sql);
        while(rs.next())
        {
            Thuoc thuoc=new Thuoc();
            thuoc.setMa(rs.getInt("Ma_Thuoc"));
            thuoc.setTenThuoc(rs.getString("Ten_Thuoc"));
            thuoc.setCongDung(rs.getString("Cong_Dung"));
            thuoc.setGiaThuoc(rs.getFloat("Gia_Thuoc"));
            thuoc.setDonVi(rs.getString("Don_Vi"));
            if(rs.getInt("So_Luong")>0)
            {
                thuoc.setTrangThai("Còn");
            }
            else  thuoc.setTrangThai("Hết");
            ThuocData.add(thuoc);
            
        }
        rs.close();
    }
    public void initTable() throws SQLException
    {
      ColThuocMa.setCellValueFactory(new PropertyValueFactory<>("ma"));
      ColThuocTenthuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
      ColThuocGiathuoc.setCellValueFactory(new PropertyValueFactory<>("giaThuoc"));
      ColThuocCongdung.setCellValueFactory(new PropertyValueFactory<>("congDung"));
      ColThuocTrangthai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
      ColThuocDonvi.setCellValueFactory(new PropertyValueFactory<>("donVi"));
      
      FilteredList<Thuoc> filteredData= new  FilteredList<>(ThuocData, p-> true);
      ThuocTable.setItems(filteredData);
      tfLoc.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)-> {
            filteredData.setPredicate((Thuoc thuoc) -> {
                if(newValue==null||newValue.isEmpty()){return true;}
                String lowerCaseFilter =newValue.toLowerCase();
                switch(cbLoc.getValue()) {
                    case "Mã":
                        if (thuoc.getMa() == Integer.parseInt(newValue)){
                            return true;
                        }
                        break;
                    case "Tên Thuốc":
                        if (thuoc.getTenThuoc().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                    case "Giá":
                        if (thuoc.getGiaThuoc() == Float.parseFloat(newValue)){
                            return true;
                        }
                    case "Công Dụng":
                        if (thuoc.getCongDung().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                }
                return false;
        });
        });
      
    }
    private void cbSearchInit()
    {
        cbLoc.getItems().addAll("Mã","Tên Thuốc","Giá","Công Dụng");
        cbLoc.setValue("Mã");
    }
    //override tu Initializable interface
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       try {
        con =  new ConnectToDatabase();
        cbSearchInit();
            addThuocData();
            initTable();
         } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    //override tu PaneInterface interface

    @Override
    public void setScreenParent(BacSiController mainPane) {
        parentPane = mainPane;
    }

}
