/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.LeTan;

import cm.ConnectToServer;
import cm.model.BenhNhan;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.sql.rowset.CachedRowSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author XyzC
 */
public class ThanhToanController implements Initializable {
    
    ConnectToServer con;
    //private PreparedStatement ps;
   // private ResultSet rs;
    private static float TienThuoc = 0,TienDV = 0,TongTien = 0;
    
    @FXML
    private ComboBox<String> cbSearchDay;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private TextField SearchText;
    @FXML
    private TableView<BenhNhan> BenhNhanTable;
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
    private TableColumn ThoiGianColumn;
    @FXML
    private TableColumn TrangThaiColumn;
    @FXML
    private Label lblTen;
    @FXML
    private Label lblDiaChia;
    @FXML
    private Label lblPhone;
    @FXML
    private Label lblGioiTinh;
    @FXML
    private Label lblNgaySinh;
    @FXML
    private Label lblDiaChi;
    @FXML
    private Label lblTienThuoc;
    @FXML
    private Label lblTienDV;
    @FXML
    private Label lblTongTien;
    private static int mabn;
    private BenhNhan bnsel;
    
    private ObservableList<BenhNhan> BenhNhanData = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            cbSearchC();
            cbSearchD();
            addBenhNhanData();
            initTable();
        } catch (SQLException ex) {
            Logger.getLogger(ThanhToanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    private void cbSearchC() {
        cbSearch.getItems().addAll("Mã","Họ Tên");
        cbSearch.setValue("Họ Tên");
    }
    private void cbSearchD() {
        cbSearchDay.getItems().addAll("Hôm Nay","Tất Cả");
        cbSearchDay.setValue("Tất cả");
    }
    public void initTable(){
        MaColumn.setCellValueFactory(new PropertyValueFactory<>("Ma"));
        TenColumn.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        NgaySinhColumn.setCellValueFactory(new PropertyValueFactory<>("NgaySinh"));
        GioiTinhColumn.setCellValueFactory(new PropertyValueFactory<>("GioiTinh"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        ThoiGianColumn.setCellValueFactory(new PropertyValueFactory<>("ThoiGian"));
        TrangThaiColumn.setCellValueFactory(new PropertyValueFactory<>("TrangThai"));
        
         FilteredList<BenhNhan> filteredData = new FilteredList<>(BenhNhanData, p -> true);
        
       cbSearchDay.valueProperty().addListener((observable, oldValue , newValue) -> {
           filteredData.setPredicate(benhnhan -> {
               switch(newValue){
                   case "Hôm Nay":
                       if (benhnhan.getThoiGian().contains(LocalDate.now().toString()))
                           return true;
                       break;
                   case "Tất Cả":
                       return true;
               }
               return false;
           });
       });
       FilteredList<BenhNhan> filteredData2 = new FilteredList<>(filteredData , p->true);
        SearchText.textProperty().addListener((observable, oldValue , newValue) -> {
            filteredData2.setPredicate( benhnhan -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }                
                String lowerCaseFilter = newValue.toLowerCase();
                switch(cbSearch.getValue()) {
                    case "Mã":
                        if (benhnhan.getMa() == Integer.parseInt(newValue)){
                            return true;
                        }
                        break;
                    case "Họ Tên":
                        if (benhnhan.getHoTen().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                }
                       
                
                return false;
            });
        });
        BenhNhanTable.setItems(filteredData2);
        BenhNhanTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
            try {
                showDetails(newValue);
            } catch (SQLException ex) {
                Logger.getLogger(ThanhToanController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
    }
    private void showDetails(BenhNhan benhnhan) throws SQLException {
            
            String sql1 = "select sum(Gia_Dich_Vu) as tien from Dich_Vu natural join Don_Dich_Vu natural join Phien_Kham"
                    +" where Ma_Benh_Nhan = ? and Ma_Phien_Kham not in(select Ma_Phien_Kham from Thanh_Toan) having tien>0;";
            String sql2 = "select sum(Chi_Phi_Thuoc) as abcd from Don_Thuoc natural join Phien_Kham where Ma_Benh_Nhan = ? and Ma_Phien_Kham not in(select Ma_Phien_Kham from Thanh_Toan) having abcd > 0;";
            con = new ConnectToServer();
            con.sendToServer(sql1);
            Object ob = con.receiveFromServer();
            if (ob.getClass().toString().equals("class java.lang.String")) 
                TienDV = 0;
            else
            {
                CachedRowSet rs = (CachedRowSet)ob;
                rs.next();
                TienDV = rs.getFloat("tien");
            }
            
            lblTienDV.setText(Float.toString(TienDV));
            con.sendToServer("done");
         
            
            con = new ConnectToServer();
            con.sendToServer(sql2);
            Object ob1 = con.receiveFromServer();
           if (ob1.getClass().toString().equals("class java.lang.String")) 
                TienThuoc = 0;
            else
            {
                CachedRowSet rs = (CachedRowSet)ob;
                rs.next();
                TienDV = rs.getFloat("abcd");
            }
            con.sendToServer("done");
            lblTienThuoc.setText(Float.toString(TienThuoc));
            
            TongTien = TienThuoc + TienDV;
            lblTongTien.setText(Float.toString(TongTien));
              
            lblTen.setText(benhnhan.getHoTen());
            mabn = benhnhan.getMa();
            lblNgaySinh.setText(benhnhan.getNgaySinh());
            lblGioiTinh.setText(benhnhan.getGioiTinh());
            lblPhone.setText(benhnhan.getPhone());
            lblDiaChi.setText(benhnhan.getDiaChi());
            bnsel = benhnhan;
            }
    public void addBenhNhanData() throws SQLException{
        String sql = "SELECT * FROM Benh_Nhan natural join (select * from Phien_Kham order by Thoi_Gian_Kham desc) as pk where Trang_Thai_BN = 'thanh toán' "
                        + "group by Ma_Benh_Nhan "
                        + "order by Thoi_Gian_Kham desc;";
        con = new ConnectToServer();
        con.sendToServer(sql);
        Object ob = con.receiveFromServer();
        if (ob.getClass().toString().equals("class java.lang.String")) {
            }
        else
        {
            CachedRowSet rs = (CachedRowSet)ob;
            while(rs.next()){
                BenhNhan benhnhan = new BenhNhan();
                benhnhan.setMa(rs.getInt("Ma_Benh_Nhan"));
                benhnhan.setHoTen(rs.getString("Ho_Ten_BN"));
                benhnhan.setNgaySinh(rs.getString("Ngay_Sinh_BN"));
                benhnhan.setGioiTinh(rs.getString("Gioi_Tinh_BN"));
                benhnhan.setPhone(rs.getString("SDT_BN"));
                benhnhan.setThoiGian(rs.getString("Thoi_Gian_Kham"));
                benhnhan.setTrangThai(rs.getString("Trang_Thai_BN"));
                benhnhan.setDiaChi(rs.getString("Dia_chi_BN"));
                BenhNhanData.add(benhnhan);
            }   
      
        }
         con.sendToServer("done");
    }
    @FXML
    private void ThanhToan(ActionEvent e) throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Thanh Toán");
        Parent root = FXMLLoader.load(getClass().getResource("/cm/view/LeTan/HoaDon.fxml"));
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();
        if(HoaDonController.getChange() == 1)
            {
                int index = BenhNhanData.indexOf(bnsel);
                BenhNhanData.remove(index);
                HoaDonController.setChange(0);
                clearDetail();
            }
        }
    
    
    
        
    public static int getmabn(){
        return mabn;
}
    public static float getTienDV(){
        return TienDV;
    }
    public static float getTienThuoc(){
        return TienThuoc;
    }
    public static float getTongTien(){
        return TongTien;
    }
    
    private void clearDetail() {
        lblTen.setText(null);
        lblNgaySinh.setText(null);
        lblGioiTinh.setText(null);
        lblPhone.setText(null);
        lblDiaChi.setText(null);
        TongTien = 0;
        TienDV = 0;
        TienThuoc = 0;
        lblTienDV.setText(null);
        lblTienThuoc.setText(null);
        lblTongTien.setText(null);
    }
}
