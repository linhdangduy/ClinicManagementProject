/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.LeTan;

import cm.ConnectToDatabase;
import cm.controller.DangNhap.DangNhapController;
import cm.controller.LeTan.ThanhToanController;
import cm.model.*;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author XyzC
 */
public class HoaDonController implements Initializable {
    
    ConnectToDatabase con;
    ResultSet rs;
    PreparedStatement ps;
    private int maPK;
    @FXML
    private Label lblTen;
    @FXML
    private Label lblNgaySinh;
    @FXML
    private Label lblGioiTinh;
    @FXML
    private Label lblThGiKham;
    @FXML
    private Label lblTrChung;
    @FXML
    private Label lblHuongDieuTri;
    @FXML
    private Label lblGhiChu;
    @FXML
    private TableView<KeDonThuoc> TableThuoc;
    @FXML
    private TableColumn ThuocColunm;
    @FXML
    private TableColumn SLColunm;
    @FXML
    private TableColumn ChiPhiColunm;
    @FXML
    private TableColumn CachDungColunm;
    @FXML
    private Label lblTienThuoc;
    @FXML
    private TableView<DonDichVu> TableDV;
    @FXML
    private TableColumn TenDVColunm;
    @FXML
    private TableColumn GiaDVColunm;
    @FXML
    private TableColumn KetquaColunm;
    @FXML
    private Label lblTienDV;
    @FXML
    private Label lblTongTien;
    @FXML
    private Label lblTenNV;
    
    private ObservableList<KeDonThuoc> ThuocData = FXCollections.observableArrayList();
    private ObservableList<DonDichVu> DVData = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            con = new ConnectToDatabase();
            initLabel();
            getDVData();
            getThuocData();
            initTableDV();
            initTableT();
        } catch (SQLException ex) {
            Logger.getLogger(HoaDonController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void initLabel() throws SQLException{
        String sql = "select Ho_Ten_BN , Ngay_Sinh_BN , Gioi_Tinh_BN , Thoi_Gian_Kham , Trieu_Chung , Huong_Dieu_Tri , Ghi_Chu_BA,Ma_Phien_Kham "
                +"from Benh_Nhan natural join Phien_Kham where Trang_Thai_BN ='thanh toán' and Ho_Ten_BN= ? group by Ma_Benh_Nhan  order by Thoi_Gian_Kham desc;";
        ps = con.getPS(sql);
        ps.setString(1, ThanhToanController.getbn());
        rs = ps.executeQuery();
        if(rs.isBeforeFirst())
        {
            rs.next();
            lblTen.setText(rs.getString("Ho_Ten_BN"));
            lblNgaySinh.setText(rs.getString("Ngay_Sinh_BN"));
            lblGioiTinh.setText(rs.getString("Gioi_Tinh_BN"));
            lblThGiKham.setText(rs.getString("Thoi_Gian_Kham"));
            lblTrChung.setText(rs.getString("Trieu_Chung"));
            lblHuongDieuTri.setText(rs.getString("Huong_Dieu_Tri"));
            lblGhiChu.setText(rs.getString("Ghi_Chu_BA"));
            lblTienDV.setText(Float.toString(ThanhToanController.getTienDV()));
            lblTienThuoc.setText(Float.toString(ThanhToanController.getTienThuoc()));
            lblTongTien.setText(Float.toString(ThanhToanController.getTongTien()));
            lblTenNV.setText(DangNhapController.getEmployeeName());
            maPK = rs.getInt("Ma_Phien_Kham");
        }
        rs.close();        
        ps.close();
    }
    private void initTableT(){
        ThuocColunm.setCellValueFactory(new PropertyValueFactory<>("TenThuoc"));
        SLColunm.setCellValueFactory(new PropertyValueFactory<>("SoLuong"));
        ChiPhiColunm.setCellFactory(new PropertyValueFactory<>("ChiPhiThuoc"));
        CachDungColunm.setCellFactory(new PropertyValueFactory<>("CachDungThuoc"));
        FilteredList<KeDonThuoc> filteredData = new FilteredList<>(ThuocData, p -> true);
        TableThuoc.setItems(filteredData);
    }
    private void initTableDV(){
        TenDVColunm.setCellValueFactory(new PropertyValueFactory<>("TenDichVu"));
        GiaDVColunm.setCellValueFactory(new PropertyValueFactory<>("GiaDichVu"));
        KetquaColunm.setCellFactory(new PropertyValueFactory<>("KetQua"));
        FilteredList<DonDichVu> filteredData = new FilteredList<>(DVData, p -> true);
        TableDV.setItems(filteredData);
    }
    private void getThuocData() throws SQLException{
        String sql= "select Ten_Thuoc,So_Luong_Ke,Chi_Phi_Thuoc,Cach_Dung_Thuoc from Thuoc natural join Don_Thuoc natural join Phien_Kham"
                +" where Ma_Phien_Kham = ?;";
        ps = con.getPS(sql);
        ps.setInt(1, maPK);
        rs = ps.executeQuery();
        while(rs.next()){
            KeDonThuoc thuoc = new KeDonThuoc();
            thuoc.setTenThuoc(rs.getString("Ten_Thuoc"));
            thuoc.setSoLuong(rs.getInt("So_Luong_Ke"));
            thuoc.setCachDungThuoc(rs.getString("Cach_Dung_Thuoc"));
            thuoc.setChiPhiThuoc(rs.getFloat("Chi_Phi_Thuoc"));
            ThuocData.add(thuoc);
        }
        rs.close();
        ps.close();
    }
    private void getDVData() throws SQLException{
        String sql= "select Ten_Dich_Vu,Gia_Dich_Vu,Ket_Qua from Dich_Vu natural join Don_Dich_Vu where Ma_Phien_Kham = ?;";
        ps = con.getPS(sql);
        ps.setInt(1, maPK);
        rs = ps.executeQuery();
        if(rs.isBeforeFirst())
        {
        while(rs.next()){
            DonDichVu dv = new DonDichVu();
            dv.setTenDichVu(rs.getString("Ten_Dich_Vu"));
            dv.setGiaDichVu(rs.getFloat("Gia_Dich_Vu"));
            dv.setKetQua(rs.getString("Ket_Qua"));
            DVData.add(dv);
        }
        }
        rs.close();
        ps.close();
    }
    
    @FXML
    private void BtnThanhToan(ActionEvent event) {
        Stage stage = (Stage) lblGhiChu.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void BtnHuy(ActionEvent event) {
        Stage stage = (Stage) lblGhiChu.getScene().getWindow();
        stage.close();
    }
    
}
