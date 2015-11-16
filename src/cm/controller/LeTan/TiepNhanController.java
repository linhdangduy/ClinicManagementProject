/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.LeTan;

import cm.ConnectToDatabase;
import cm.model.BenhNhan;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author vinh
 */
public class TiepNhanController implements Initializable{
    ConnectToDatabase con;
    private PreparedStatement ps;
    private ResultSet rs;
    @FXML
    private Label lblTen;
    @FXML
    private Label lblNgaySinh;
    @FXML
    private Label lblGioiTinh;
    @FXML
    private Label lblDiaChi;
    @FXML
    private Label lblPhone;
    @FXML
    private TextField tfTen1;
    @FXML
    private ComboBox<String> cbNgay1;
    @FXML
    private ComboBox<String> cbThang1;
    @FXML
    private ComboBox<String> cbNam1;
    @FXML
    private ToggleGroup groupLevel1;
    @FXML
    private RadioButton rbNam1;
    @FXML
    private RadioButton rbNu1;
    @FXML
    private TextField tfDiaChi1;
    @FXML
    private TextField tfPhone1;
    @FXML
    private TextField tfTen2;
    @FXML
    private ComboBox<String> cbNgay2;
    @FXML
    private ComboBox<String> cbThang2;
    @FXML
    private ComboBox<String> cbNam2;
    @FXML
    private ToggleGroup groupLevel2;
    @FXML
    private RadioButton rbNam2;
    @FXML
    private RadioButton rbNu2;
    @FXML
    private TextField tfDiaChi2;
    @FXML
    private TextField tfPhone2;
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
    private ComboBox<String> cbSearch;
    @FXML
    private TextField tfFilter;
    
    private String gioitinh1;
    private String gioitinh2;
    private String ngaysinh = "";
    private BenhNhan bnSelected;
    
    private ObservableList<BenhNhan> BenhNhanData = FXCollections.observableArrayList();
    private ObservableList<String> ngay = FXCollections.observableArrayList();
    private ObservableList<String> thang = FXCollections.observableArrayList();
    private ObservableList<String> nam = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con = new ConnectToDatabase();
            cbSearchInit();
            cbNgaySinhInit();
            rbNam1.setOnAction(e -> gioitinh1 = "Nam");
            rbNu1.setOnAction(e -> gioitinh1 = "Nữ");
            rbNam2.setOnAction(e -> gioitinh2 = "Nam");
            rbNu2.setOnAction(e -> gioitinh2 = "Nữ");
            addBenhNhanData();
            initTable();
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void addBenhNhanData() throws SQLException{
        String sql = "SELECT * FROM Benh_Nhan";
        rs = con.getRS(sql);
        while(rs.next()){
            BenhNhan benhnhan = new BenhNhan();
            benhnhan.setMa(rs.getInt("Ma_Benh_Nhan"));
            benhnhan.setHoTen(rs.getString("Ho_Ten"));
            benhnhan.setNgaySinh(rs.getString("Ngay_Sinh"));
            benhnhan.setGioiTinh(rs.getString("Gioi_Tinh"));
            benhnhan.setPhone(rs.getString("SDT_BN"));
            benhnhan.setTrangThai(rs.getString("Trang_Thai"));
            benhnhan.setDiaChi(rs.getString("Dia_chi"));
            BenhNhanData.add(benhnhan);
        }
        rs.close();
    }
    private void cbSearchInit() {
        cbSearch.getItems().addAll("Mã","Họ Tên","Trạng Thái");
        cbSearch.setValue("Họ Tên");
    }
    public void initTable(){
        MaColumn.setCellValueFactory(new PropertyValueFactory<>("Ma"));
        TenColumn.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        NgaySinhColumn.setCellValueFactory(new PropertyValueFactory<>("NgaySinh"));
        GioiTinhColumn.setCellValueFactory(new PropertyValueFactory<>("GioiTinh"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        TrangThaiColumn.setCellValueFactory(new PropertyValueFactory<>("TrangThai"));
        
        
        FilteredList<BenhNhan> filteredData = new FilteredList<>(BenhNhanData, p -> true);
        BenhNhanTable.setItems(filteredData);
        tfFilter.textProperty().addListener((observable, oldValue , newValue) -> {
            filteredData.setPredicate( benhnhan -> {
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
                    case "Trạng Thái":
                        if (benhnhan.getTrangThai().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                }
                return false;
            });
        });
        //BenhNhanTable.setItems(BenhNhanData);
        BenhNhanTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));
        
        
    }
    
    private void showDetails(BenhNhan benhnhan) {
        if(benhnhan != null){
            //Show thong tin o tab thong tin
            lblTen.setText(benhnhan.getHoTen());
            lblNgaySinh.setText(benhnhan.getNgaySinh());
            lblGioiTinh.setText(benhnhan.getGioiTinh());
            lblPhone.setText(benhnhan.getPhone());
            lblDiaChi.setText(benhnhan.getDiaChi());
            
            //Show thong tin o tab chinh sua
            bnSelected = benhnhan;
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
            
        }
        else{
            lblTen.setText("");
            lblNgaySinh.setText("");
            lblGioiTinh.setText("");
            lblPhone.setText("");
            lblDiaChi.setText("");
        }
    }
    
    
    private void cbNgaySinhInit(){
        /*int[] ngay = new int[31];
        int[] thang = new int[12];
        int[] nam = new int[150];*/
        int i;
        String a;
        for (i=1;i<32;i++){
            if (i<10){
                a = "0"+i;
            }else{
                a=""+i;
            }
            ngay.add(a);
        }
        for (i=1;i<13;i++){
            if (i<10){
                a = "0"+i;
            }else{
                a=""+i;
            }
            thang.add(a);
        }
        for (i=2015;i>=1900;i--){
            a=""+i;
            nam.add(a);
        }
        cbNgay1.getItems().addAll(ngay);
        cbThang1.getItems().addAll(thang);
        cbNam1.getItems().addAll(nam);
        
        cbNgay2.getItems().addAll(ngay);
        cbThang2.getItems().addAll(thang);
        cbNam2.getItems().addAll(nam);
    }
    @FXML
    private void Them (ActionEvent e) throws IOException {
        try {
            //them vao obsevablelist
            BenhNhan benhnhan = new BenhNhan();
            benhnhan.setHoTen(tfTen1.getText());
            ngaysinh = "" + cbNam1.getValue();
            ngaysinh = ngaysinh +"-";
            ngaysinh = ngaysinh + cbThang1.getValue();
            ngaysinh = ngaysinh +"-";
            ngaysinh = ngaysinh + cbNgay1.getValue();
            benhnhan.setNgaySinh(ngaysinh);
            benhnhan.setGioiTinh(gioitinh1);
            benhnhan.setDiaChi(tfDiaChi1.getText());
            benhnhan.setPhone(tfPhone1.getText());
            benhnhan.setTrangThai("phòng khám");
            //Them thong tin benh nhan vao co so du lieu
            String sql = "insert into Benh_Nhan values (NULL,?,?,?,?,?,?);";
            ps.close();
            ps = con.getPS(sql);
            ps.setString(1, benhnhan.getHoTen());
            ps.setString(2, benhnhan.getNgaySinh());
            ps.setString(3, benhnhan.getDiaChi());
            ps.setString(4, benhnhan.getGioiTinh());
            ps.setString(5, benhnhan.getPhone());
            ps.setString(6, benhnhan.getTrangThai());
            ps.executeUpdate();
            ps.close();
            // lay ma benhn nhan da nhap
            sql = "select * from `Benh_Nhan` where Ho_Ten = ? and SDT_BN = ?;";
            ps = con.getPS(sql);
            ps.setString(1, benhnhan.getHoTen());
            ps.setString(2, benhnhan.getPhone());
            rs = ps.executeQuery();
            rs.next();
            benhnhan.setMa(rs.getInt("Ma_Benh_Nhan"));
            ps.close();
            
                
            //them thoi gian kham vao bang Phien_Kham cho benh nhan
            PreparedStatement ps2 = con.getPS(
                    "INSERT INTO Phien_Kham(Ma_Benh_Nhan, Thoi_Gian_Kham) VALUES (?,?)");
            ps2.setInt(1, rs.getInt("Ma_Benh_Nhan"));
            long timeNow = Calendar.getInstance().getTimeInMillis();
            Timestamp ts = new java.sql.Timestamp(timeNow);
            ps2.setTimestamp(2, ts);
            ps2.executeUpdate();
            benhnhan.setThoiGian(ts.toString());
            BenhNhanData.add(benhnhan);
            refreshTabThemMoi();
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void refreshTabThemMoi(){
        tfTen1.setText("");
        cbNgay1.setValue("dd");
        cbThang1.setValue("mm");
        cbNam1.setValue("yyyy");
        rbNam1.setSelected(false);
        rbNu1.setSelected(false);
        gioitinh1 = "";
        tfDiaChi1.setText("");
        tfPhone1.setText("");
    }
   
    @FXML 
    private void Huy(ActionEvent e) throws IOException {
        refreshTabThemMoi();
    }
        
    @FXML
    private void Sua(ActionEvent e) throws IOException{
        try {
            //cap nhat vao du lieu
            String sql = "Update Benh_Nhan set Ho_Ten = ?, Ngay_Sinh = ? , Dia_Chi = ? , Gioi_Tinh = ?, SDT_BN = ? where Ma_Benh_Nhan = ?";
            ps = con.getPS(sql);
            ngaysinh = "" + cbNam2.getValue() + "-" + cbThang2.getValue() + "-" +cbNgay2.getValue();
            ps.setString(1, tfTen2.getText());
            ps.setString(2, ngaysinh);
            ps.setString(3, tfDiaChi2.getText());
            ps.setString(4, gioitinh2);
            ps.setString(5, tfPhone2.getText());
            ps.setInt(6, bnSelected.getMa());
            ps.executeUpdate();
            ps.close();
            
            //cap nhat vao bang
            bnSelected.setHoTen(tfTen2.getText());
            bnSelected.setNgaySinh(ngaysinh);
            bnSelected.setDiaChi(tfDiaChi2.getText());
            bnSelected.setGioiTinh(gioitinh2);
            bnSelected.setPhone(tfPhone2.getText());
            int index = BenhNhanData.indexOf(bnSelected);
            BenhNhanData.set(index, bnSelected);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void Xoa(ActionEvent e) throws IOException{
        try {
            String sql = "delete from Benh_Nhan where Ma_Benh_Nhan = ?";
            ps = con.getPS(sql);
            ps.setInt(1, bnSelected.getMa());
            ps.executeUpdate();
            ps.close();
            BenhNhanData.remove(bnSelected);
            bnSelected = null;
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
