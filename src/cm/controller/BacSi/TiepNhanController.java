/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

import cm.ConnectToDatabase;
import cm.controller.DangNhap.DangNhapController;
import cm.model.BenhNhan;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
/**
 *
 * @author linhsan
 */
public class TiepNhanController implements Initializable, PaneInterface {
    
    private ConnectToDatabase con;
    private ResultSet rs;
    private BacSiController parentPane;
    @FXML
    private Button btnChiTiet;
    @FXML
    private VBox boxDanhSachBN;
    @FXML
    private TabPane tabDieuTri;
    @FXML
    private Label lblHoTen;
    @FXML
    private Label lblNgaySinh;
    @FXML
    private Label lblGioiTinh;
    @FXML
    private Label lblDiaChi;
    @FXML
    private Label lblSoDienThoai;
    @FXML
    private TableView<BenhNhan> tblBenhNhan;
    @FXML
    private TableColumn colMa, colHoten, colNgaysinh, colGioitinh, 
            colSdt, colThoigiankham, colTrangthai;
    private ObservableList<BenhNhan> benhnhanData = FXCollections.observableArrayList();
    private ObservableList<BenhNhan> filteredData = FXCollections.observableArrayList();
    
    //override tu Initializable interface
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createConnection();
        initTable();
    }
    
    public void initTable() {
        colMa.setCellValueFactory(new PropertyValueFactory<>("Ma"));
        colHoten.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        colNgaysinh.setCellValueFactory(new PropertyValueFactory<>("NgaySinh"));
        colGioitinh.setCellValueFactory(new PropertyValueFactory<>("GioiTinh"));
        colSdt.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        colThoigiankham.setCellValueFactory(new PropertyValueFactory<>("ThoiGian"));
        colTrangthai.setCellValueFactory(new PropertyValueFactory<>("TrangThai"));
        
        tblBenhNhan.setItems(benhnhanData);
    }
    
    @FXML
    private void handleBtnBenhNhanTiep(ActionEvent event) {
        
    }
    
    @FXML
    private void handleBtnChiTiet(ActionEvent event) {
        tabDieuTri.setVisible(true);
        boxDanhSachBN.setVisible(false);
    }
    @FXML
    private void handleBtnLuu(ActionEvent event) {
        
    }
    @FXML
    private void handleBtnTroLai(ActionEvent event) {
        tabDieuTri.setVisible(false);
        boxDanhSachBN.setVisible(true);
    }
    
    @FXML
    private void handleBtnKeDon(ActionEvent event) {
        parentPane.setPane("thuoc");
    }
    @FXML
    private void handleBtnSuDung(ActionEvent event) {
        parentPane.setPane("dichvu");
    }
    
    
    //override tu PaneInterface interface
    @Override
    public void setScreenParent(BacSiController mainPane) {
        parentPane = mainPane;
    }
    
    private void createConnection() {
        try {           
            con = new ConnectToDatabase();
            String query = "select * from Benh_Nhan";
            rs = con.getRS(query);
            while (rs.next()) {
               BenhNhan bn = new BenhNhan();
               bn.setMa(rs.getInt(1));
               bn.setHoTen(rs.getString(2));
               bn.setNgaySinh(rs.getString(3));
               bn.setDiaChi(rs.getString(4));
               bn.setGioiTinh(rs.getString(5));
               bn.setPhone(rs.getString(6));
               bn.setTrangThai(rs.getString(7));
               benhnhanData.add(bn);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
