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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
/**
 *
 * @author linhsan
 */
public class TiepNhanController implements Initializable, PaneInterface {
    
    private ConnectToDatabase con;
    private ResultSet rs;
    private PreparedStatement ps;
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
    private Label lblCanhBao;
    @FXML
    private ComboBox<String> cbLoctheo;
    @FXML
    private TextField tfLoc;
    @FXML
    private TextField tfTrieuChung, tfTenBenh;
    @FXML
    private TextArea taGhiChu, taHuongDieuTri, taKeDonThuoc, taSuDungDichVu;
    @FXML
    private TableView<BenhNhan> tblBenhNhan;
    @FXML
    private TableColumn colMa, colHoten, colNgaysinh, colGioitinh, 
            colSdt, colThoigiankham, colTrangthai;
    private ObservableList<BenhNhan> benhnhanData = FXCollections.observableArrayList();
    private BenhNhan benhnhanDangKham;
    
    //override tu Initializable interface
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createConnection();
        initTable();
        initCbLoc();
    }
    
    private void initCbLoc() {
        cbLoctheo.getItems().addAll("Mã", "Họ tên", "Số điện thoại");
        cbLoctheo.setValue("Họ tên");
    }
    private void initTable() {
        colMa.setCellValueFactory(new PropertyValueFactory<>("Ma"));
        colHoten.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        colNgaysinh.setCellValueFactory(new PropertyValueFactory<>("NgaySinh"));
        colGioitinh.setCellValueFactory(new PropertyValueFactory<>("GioiTinh"));
        colSdt.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        colThoigiankham.setCellValueFactory(new PropertyValueFactory<>("ThoiGian"));
        colTrangthai.setCellValueFactory(new PropertyValueFactory<>("TrangThai"));
        
        
        FilteredList<BenhNhan> filteredData = new FilteredList<>(benhnhanData, p -> true);
        tblBenhNhan.setItems(filteredData);
        tfLoc.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(bn -> {
                // If filter text is empty, display all
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (cbLoctheo.getValue()) {
                    case "Mã":
                        if (bn.getMa() == Integer.parseInt(newValue)) {
                            return true; 
                        }
                    break;
                    case "Họ tên":
                        if (bn.getHoTen().toLowerCase().contains(lowerCaseFilter)) {
                            return true; 
                        }
                    break;
                    case "Số điện thoại":
                        if (bn.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                            return true; 
                        }
                    break;
                }
                return false;
            });
        });
        tblBenhNhan.getSelectionModel().selectedItemProperty().addListener(
                observable -> showDetails(tblBenhNhan.getSelectionModel().getSelectedItem()));
    }
    
    private void showDetails(BenhNhan benhnhan) {
        if(benhnhan != null){
            //Show thong tin o tab thong tin
            lblHoTen.setText(benhnhan.getHoTen());
            lblNgaySinh.setText(benhnhan.getNgaySinh());
            lblGioiTinh.setText(benhnhan.getGioiTinh());
            lblSoDienThoai.setText(benhnhan.getPhone());
            lblDiaChi.setText(benhnhan.getDiaChi());
            
            //Show thong tin o tab chinh sua
        /*    bnSelected = benhnhan;
            tfTen2.setText(benhnhan.getHoTen());
            //Cat xau ngaysinh tra ket qua ngay, thang, nam
            ngaysinh = benhnhan.getNgaySinh();
            String result[] = ngaysinh.split("[-]");
            int i = 1;
            for (String tmp : result){
                if (i==1) cbNam2.setValue(tmp);
                else if (i==2) cbThang2.setValue(tmp);
                else if (i==3) cbNgay2.setValue(tmp);
                i++;
            }
            
            switch (benhnhan.getGioiTinh()){
                case "Nam":
                    rbNam2.setSelected(true);
                    gioitinh2 = "Nam";
                    break;
                case "Nữ":
                    rbNu2.setSelected(true);
                    gioitinh2 = "Nữ";
                    break;
            }
            tfDiaChi2.setText(benhnhan.getDiaChi());
            tfPhone2.setText(benhnhan.getPhone());
        */    
        }
        else{
            lblHoTen.setText("");
            lblNgaySinh.setText("");
            lblGioiTinh.setText("");
            lblSoDienThoai.setText("");
            lblDiaChi.setText("");
        }
    }
    
    @FXML
    private void handleBtnBenhNhanTiep(ActionEvent event) {
        
    }
    
    @FXML
    private void handleBtnChiTiet(ActionEvent event) {
        if (tblBenhNhan.getSelectionModel().getSelectedItem() != null) {
            tabDieuTri.setVisible(true);
            boxDanhSachBN.setVisible(false);
            lblCanhBao.setText(null);
        }
        else {
            lblCanhBao.setText("Chưa chọn bệnh nhân nào!");
        }
    }
    @FXML
    private void handleBtnLuu(ActionEvent event) {
        try {
            String query = 
                    "UPDATE Phien_Kham SET Ten_Benh = ?, Trieu_Chung = ?, "
                    + "Huong_Dieu_Tri = ?, Ghi_Chu_BA = ? WHERE Ma_Benh_Nhan = ? AND "
                    + "Ten_Benh IS NULL AND Trieu_Chung IS NULL AND Huong_Dieu_Tri IS NULL AND Ghi_Chu_BA IS NULL";
            ps = con.getPS(query);
            ps.setString(1, tfTenBenh.getText());
            ps.setString(2, tfTrieuChung.getText());
            ps.setString(3, taHuongDieuTri.getText());
            ps.setString(4, taGhiChu.getText());
            ps.setInt(5, tblBenhNhan.getSelectionModel().getSelectedItem().getMa());
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    @FXML
    private void handleBtnTroLai(ActionEvent event) {
        tabDieuTri.setVisible(false);
        boxDanhSachBN.setVisible(true);
    }
    
    @FXML
    private void handleBtnKeDon(ActionEvent event) {
        parentPane.setPane("thuoc");
        benhnhanDangKham = tblBenhNhan.getSelectionModel().getSelectedItem();
    }    
    
    @FXML
    private void handleBtnSuDung(ActionEvent event) {
        parentPane.setPane("dichvu");
        benhnhanDangKham = tblBenhNhan.getSelectionModel().getSelectedItem();
    }
    
    public BenhNhan getBenhNhan() {
        return benhnhanDangKham;
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
                if (rs.getString(7).equals("phòng khám")) {
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
