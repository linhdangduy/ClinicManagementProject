/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.QuanLy;

import cm.ConnectToServer;
import cm.controller.DangNhap.DangNhapController;
import cm.controller.QuanLy.QuanLyController;
import cm.model.BenhNhan;
import cm.model.NhanVien;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.sql.rowset.CachedRowSet;

/**
 * FXML Controller class
 *
 * @author XyzC
 */
public class TiepNhanController implements Initializable {
    
    ConnectToServer con;
    
    @FXML
    private Label lblTenDangNhap;
    @FXML
    private Label lblNguoiDung;
    @FXML
    private Label lblBacHoc;
    @FXML
    private Label lblTrangThai;
    @FXML
    private Label lblChuyenNganh;
    @FXML
    private Label lblPhong;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnDelete;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private TextField tfFilter;
    @FXML
    private TableView<NhanVien> NhanVienTable;
    @FXML
    private TableColumn<NhanVien,String> MaColumn;
    @FXML
    private TableColumn<NhanVien,String> TenColumn;
    @FXML
    private TableColumn<NhanVien,String> NgaySinhColumn;
    @FXML
    private TableColumn<NhanVien,String> GioiTinhColumn;
    @FXML
    private TableColumn<NhanVien,String> PhoneColumn;
    @FXML
    private TableColumn<NhanVien,String> colTrangThai;
    
    private ObservableList<NhanVien> NhanVienData = FXCollections.observableArrayList();
    private NhanVien nhanviensel;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
            btnAccept.setDisable(true);
            lblTrangThai.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if("Đăng Ký".equals(lblTrangThai.getText()))
                    btnAccept.setDisable(false);
                else
                    btnAccept.setDisable(true);
            });
            
            cbSearchInit();
        try {
            addNhanVienData();
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
            Table();

    }
    
    private void cbSearchInit() {
    cbSearch.getItems().addAll("Giới Tính","Họ Tên","Trạng Thái");
    cbSearch.setValue("Họ Tên");
    }
    public void Table(){
        MaColumn.setCellValueFactory(new PropertyValueFactory<>("Tendangnhap"));
        TenColumn.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        NgaySinhColumn.setCellValueFactory(new PropertyValueFactory<>("NgaySinh"));
        GioiTinhColumn.setCellValueFactory(new PropertyValueFactory<>("GioiTinh"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("Trangthai"));

        FilteredList<NhanVien> filteredData = new FilteredList<>(NhanVienData, p -> true);
        NhanVienTable.setItems(filteredData);
        tfFilter.textProperty().addListener((observable, oldValue , newValue) -> {
            filteredData.setPredicate( nhanvien -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                switch(cbSearch.getValue()) {
                    case "Giới Tính":
                        if (nhanvien.getGioiTinh().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                    case "Họ Tên":
                        if (nhanvien.getHoTen().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                    case "Trạng Thái":
                        if (nhanvien.getTrangthai().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                }
                return false;
            });
        });
        NhanVienTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));
        
        
    }        
    public void addNhanVienData () throws SQLException{
        String sql = "SELECT * FROM Tai_Khoan WHERE Ten_Dang_Nhap != '" + DangNhapController.getTenDangNhap() + "' ;";
        con = new ConnectToServer();
        con.sendToServer(sql);
        Object ob = con.receiveFromServer();
        if (ob.getClass().toString().equals("class java.lang.String")) {
                 con.sendToServer("done");
            }
        else
        {
            CachedRowSet rs = (CachedRowSet)ob;
            while(rs.next())
            {
                NhanVien nhanvien = new NhanVien();
                nhanvien.setTendangnhap(rs.getString("Ten_Dang_Nhap"));
                nhanvien.setHoTen(rs.getString("Ten_Nhan_Vien"));
                nhanvien.setNgaySinh(rs.getString("Ngay_Sinh"));
                nhanvien.setDiaChi(rs.getString("Dia_Chi"));
                nhanvien.setGioiTinh(rs.getString("Gioi_Tinh"));
                nhanvien.setPhone(rs.getString("SDT"));
                nhanvien.setChuyennganh(rs.getString("Chuyen_Nganh"));
                nhanvien.setBachoc(rs.getString("Bac_Hoc"));
                nhanvien.setPhong(rs.getString("Phong"));
                nhanvien.setTrangthai(rs.getString("Trang_Thai"));
                NhanVienData.add(nhanvien);
            }
            con.sendToServer("done");
        }
    }
    
    private void showDetails(NhanVien nhanvien) {
        nhanviensel = nhanvien;
        lblTenDangNhap.setText(nhanvien.getTendangnhap());
        lblNguoiDung.setText(nhanvien.getHoTen());
        lblBacHoc.setText(nhanvien.getBachoc());
        lblChuyenNganh.setText(nhanvien.getChuyennganh());
        lblPhong.setText(nhanvien.getPhong());
        lblTrangThai.setText(nhanvien.getTrangthai());
    }

    @FXML
    private void Accept(ActionEvent event) throws SQLException {
        
        String sql= "UPDATE Tai_Khoan SET Trang_Thai = 'Nghỉ' WHERE Ten_Dang_Nhap = '"+nhanviensel.getTendangnhap()+"';";
        con = new ConnectToServer();
        con.sendToServer(sql);
        con.sendToServer("done");
        int index = NhanVienData.indexOf(nhanviensel);
        nhanviensel.setTrangthai("Nghỉ");
        NhanVienData.set(index, nhanviensel);
        showDetails(nhanviensel);
    }

    @FXML
    private void Delete(ActionEvent event) throws SQLException {
        
        String sql = "DELETE FROM Tai_Khoan WHERE Ten_Dang_Nhap = '"+ nhanviensel.getTendangnhap()+"';";
        String notice = "Xác nhận xoá tài khoản "+ lblTenDangNhap.getText()+"?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION , notice, ButtonType.YES, ButtonType.NO  );
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.showAndWait();
        
        if(alert.getResult() == ButtonType.YES){
            con = new ConnectToServer();
            con.sendToServer(sql);
            con.sendToServer("done");
            int index =  NhanVienData.indexOf(nhanviensel);
            NhanVienData.remove(index);
            //nhanviensel = new NhanVien();
            //showDetails(nhanviensel);
        }
                }
    
}
