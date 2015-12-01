/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.QuanLy;

import cm.ConnectToDatabase;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author XyzC
 */
public class TiepNhanController implements Initializable {
    
    ConnectToDatabase con;
    private ResultSet rs;
    private PreparedStatement ps;
    
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            con = new ConnectToDatabase();
            
            btnDelete.setDisable(true);
            btnAccept.setDisable(true);
            lblTrangThai.textProperty().addListener(new ChangeListener<String>(){

                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if("Đăng Ký".equals(lblTrangThai.getText()))
                    {
                        btnDelete.setDisable(false);
                        btnAccept.setDisable(false);
                    }
                    else
                    {
                        btnDelete.setDisable(true);
                        btnAccept.setDisable(true);
                    }
                }
                
            });
            
            cbSearchInit();
            addNhanVienData();
            Table();
        } catch (SQLException ex) {
            Logger.getLogger(QuanLyController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        String sql = "SELECT * FROM Tai_Khoan;";
        rs = con.getRS(sql);
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
        rs.close();
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
        
        String sql= "UPDATE Tai_Khoan SET Trang_Thai = 'Nghỉ' WHERE Ten_Dang_Nhap = ?";
        ps = con.getPS(sql);
        ps.setString(1,lblTenDangNhap.getText());
        ps.executeUpdate();
        ps.close(); 
        int index = NhanVienData.indexOf(nhanviensel);
        nhanviensel.setTrangthai("Nghỉ");
        NhanVienData.set(index, nhanviensel);
        showDetails(nhanviensel);
    }

    @FXML
    private void Delete(ActionEvent event) throws SQLException {
        
        String sql = "DELETE FROM Tai_Khoan WHERE Ten_Dang_Nhap = ?";
        ps=con.getPS(sql);
        ps.setString(1,lblTenDangNhap.getText());
        ps.executeUpdate();
        ps.close();
        int index =  NhanVienData.indexOf(nhanviensel);
        NhanVienData.remove(index);
        nhanviensel = new NhanVien();
        showDetails(nhanviensel);
                }
    
}
