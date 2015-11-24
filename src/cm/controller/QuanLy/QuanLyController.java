package cm.controller.QuanLy;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import cm.ClinicManager;
import cm.ConnectToDatabase;
import cm.controller.DangNhap.DangNhapController;
import cm.model.NhanVien;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author linhsan
 */
public class QuanLyController implements Initializable {
    ConnectToDatabase con;
    private ResultSet rs;
    private PreparedStatement ps;
    
    @FXML
    private Label lblDangXuat;
    @FXML
    private Label lblTenDangNhap;
    @FXML
    private Label lblNguoiDung;
    @FXML
    private Label lblNgaySinh;
    @FXML
    private Label lblGioiTinh;
    @FXML
    private Label lblDiaChi;
    @FXML
    private Label lblSoDienThoai;
    @FXML
    private Label lblChuyenNganh;
    @FXML
    private Label lblBacHoc;
    @FXML
    private TextField tfTen1;
    @FXML
    private TextField tfDiaChi1;
    @FXML
    private TextField tfPhone1;
    @FXML
    private ComboBox<?> cbNgay1;
    @FXML
    private ComboBox<?> cbThang1;
    @FXML
    private ComboBox<?> cbNam1;
    @FXML
    private RadioButton rbNam1;
    @FXML
    private ToggleGroup groupLevel1;
    @FXML
    private RadioButton rbNu1;
    @FXML
    private TextField tfTen11;
    @FXML
    private TextField tfTen111;
    @FXML
    private TextField tfDiaChi11;
    @FXML
    private TextField tfPhone11;
    @FXML
    private ComboBox<?> cbNgay11;
    @FXML
    private ComboBox<?> cbThang11;
    @FXML
    private ComboBox<?> cbNam11;
    @FXML
    private RadioButton rbNam11;
    @FXML
    private ToggleGroup groupLevel11;
    @FXML
    private RadioButton rbNu11;
    @FXML
    private TextField tfTen1111;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private TextField tfFilter;
    @FXML
    private TableView<NhanVien> NhanVienTable;
    @FXML
    private TableColumn MaColumn;
    @FXML
    private TableColumn TenColumn;
    @FXML
    private TableColumn NgaySinhColumn;
    @FXML
    private TableColumn GioiTinhColumn;
    @FXML
    private TableColumn PhoneColumn;
    @FXML
    private TableColumn ChuyennganhColumn;
    @FXML
    private TableColumn BachocColumn;
    
    private ObservableList<NhanVien> NhanVienData = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            con = new ConnectToDatabase();
            cbSearchInit();
            addNhanVienData();
            Table();
        } catch (SQLException ex) {
            Logger.getLogger(QuanLyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cbSearchInit() {
    cbSearch.getItems().addAll("Chuyên Ngành","Họ Tên","Bậc Học");
    cbSearch.setValue("Họ Tên");
    }
    public void Table(){
        MaColumn.setCellValueFactory(new PropertyValueFactory<>("Tendangnhap"));
        TenColumn.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        NgaySinhColumn.setCellValueFactory(new PropertyValueFactory<>("NgaySinh"));
        GioiTinhColumn.setCellValueFactory(new PropertyValueFactory<>("GioiTinh"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        ChuyennganhColumn.setCellValueFactory(new PropertyValueFactory<>("Chuyennganh"));
        BachocColumn.setCellValueFactory(new PropertyValueFactory<>("Bachoc"));

        FilteredList<NhanVien> filteredData = new FilteredList<>(NhanVienData, p -> true);
        NhanVienTable.setItems(filteredData);
        tfFilter.textProperty().addListener((observable, oldValue , newValue) -> {
            filteredData.setPredicate( nhanvien -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                switch(cbSearch.getValue()) {
                    case "Chuyên Ngành":
                        if (nhanvien.getChuyennganh().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                    case "Họ Tên":
                        if (nhanvien.getHoTen().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                    case "Bậc Học":
                        if (nhanvien.getBachoc().toLowerCase().contains(lowerCaseFilter)){
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
            NhanVienData.add(nhanvien);
        }
        rs.close();
    }
    
    private void showDetails(NhanVien nhanvien) {
        lblTenDangNhap.setText(nhanvien.getTendangnhap());
        lblNguoiDung.setText(nhanvien.getHoTen());
        lblBacHoc.setText(nhanvien.getBachoc());
        lblChuyenNganh.setText(nhanvien.getChuyennganh());
        lblDiaChi.setText(nhanvien.getDiaChi());
        lblGioiTinh.setText(nhanvien.getGioiTinh());
        lblSoDienThoai.setText(nhanvien.getPhone());
        lblNgaySinh.setText(nhanvien.getNgaySinh());
    }

    
    @FXML
    private void mouseClickedLblDangXuat(MouseEvent event) {
        if(event.getSource() == lblDangXuat)
            setView("/cm/view/DangNhap/DangNhap.fxml");
    }
    private void setView(String url) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(url));
            Scene scene = new Scene(root);
            ClinicManager.getStage().setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Them(ActionEvent event) {
    }

    @FXML
    private void Huy(ActionEvent event) {
    }

    @FXML
    private void Sua(ActionEvent event) {
    }

    @FXML
    private void Xoa(ActionEvent event) {
    }
    
}
