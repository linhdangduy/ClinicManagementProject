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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
    DatePicker date1;
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
    DatePicker date2;
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
    private BenhNhan bnSelected;
    
    private ObservableList<BenhNhan> BenhNhanData = FXCollections.observableArrayList();
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con = new ConnectToDatabase();
            cbSearchInit();
            //cbNgaySinhInit();
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
        //hien thi benh nhan theo thu tu co thoi gian kham gan nhat
        String sql = "SELECT * FROM Benh_Nhan natural join (select * from Phien_Kham order by Thoi_Gian_Kham desc) as pk "
                        + "group by Ma_Benh_Nhan "
                        + "order by Thoi_Gian_Kham desc;";
        BenhNhanData.clear();
        rs = con.getRS(sql);
        while(rs.next()){
            BenhNhan benhnhan = new BenhNhan();
            benhnhan.setMa(rs.getInt("Ma_Benh_Nhan"));
            benhnhan.setHoTen(rs.getString("Ho_Ten"));
            benhnhan.setNgaySinh(rs.getString("Ngay_Sinh"));
            benhnhan.setGioiTinh(rs.getString("Gioi_Tinh"));
            benhnhan.setPhone(rs.getString("SDT_BN"));
            benhnhan.setThoiGian(rs.getString("Thoi_Gian_Kham"));
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
        ThoiGianColumn.setCellValueFactory(new PropertyValueFactory<>("ThoiGian"));
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
        
        BenhNhanTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));
        
        
    }
    
    private void showDetails(BenhNhan benhnhan) {
        bnSelected = benhnhan;
        if(benhnhan != null){
            //Show thong tin o tab thong tin
            lblTen.setText(benhnhan.getHoTen());
            lblNgaySinh.setText(benhnhan.getNgaySinh());
            lblGioiTinh.setText(benhnhan.getGioiTinh());
            lblPhone.setText(benhnhan.getPhone());
            lblDiaChi.setText(benhnhan.getDiaChi());
            
            //Show thong tin o tab chinh sua
            
            tfTen2.setText(benhnhan.getHoTen());
            
           
            
            date2.setValue(LocalDate.parse(benhnhan.getNgaySinh(), DateTimeFormatter.ISO_DATE));
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
    
    
    @FXML
    private void taoPhienKhamMoi(ActionEvent e) throws IOException{
        if (bnSelected.getTrangThai().equals("kết thúc")){
            Alert alert = new Alert(AlertType.CONFIRMATION, "Xác Nhận Tạo Phiên Khám Mới Cho Bệnh Nhân?", ButtonType.YES , ButtonType.NO);
            alert.setTitle("Xác Nhận");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES){
                try {
                    String sql = "Update Benh_Nhan set Trang_Thai = ? where Ma_Benh_Nhan = ? ";
                    ps =con.getPS(sql);
                    ps.setString(1, "phòng khám");
                    ps.setInt(2, bnSelected.getMa());
                    ps.executeUpdate();
                    ps.close();
                    
                    //tao phien kham moi
                    PreparedStatement ps2 = con.getPS("INSERT INTO Phien_Kham(Ma_Benh_Nhan, Thoi_Gian_Kham) VALUES (?,?)");
                    ps2.setInt(1, bnSelected.getMa());
                    long timeNow = Calendar.getInstance().getTimeInMillis();
                    Timestamp ts = new java.sql.Timestamp(timeNow);
                    ps2.setTimestamp(2, ts);
                    ps2.executeUpdate();
                    
                    //cập nhật bảng
                    
                    int index = BenhNhanData.indexOf(bnSelected);
                    BenhNhan benhnhan = bnSelected;
                    benhnhan.setThoiGian(ts.toString());
                    benhnhan.setTrangThai("phòng khám");
                    BenhNhanData.remove(index);
                    BenhNhanData.add(0, benhnhan);
                    
                    
                } catch (SQLException ex) {
                    Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
                }     
            }
        }
        else{
            Alert alert2 = new Alert(AlertType.INFORMATION,"Bệnh nhân đang trong phiên khám!", ButtonType.OK);
            alert2.setTitle("Xác Nhận");
            alert2.showAndWait();
        }
        
    }
    @FXML
    private void Them (ActionEvent e) throws IOException {
        try {
            Alert dialogStage = new Alert(AlertType.CONFIRMATION, "Bạn có xác nhận thêm bệnh nhân?", ButtonType.YES ,ButtonType.NO) ;
            dialogStage.setTitle("Xác Nhận");
            //dialogStage.setHeaderText("Bạn có xác nhận thêm bệnh nhân?");
            dialogStage.showAndWait();
            if (dialogStage.getResult() == ButtonType.YES) {
                if (!tfTen1.getText().isEmpty()
                        && !date1.getValue().toString().isEmpty()
                        && !gioitinh1.isEmpty()
                        && !tfDiaChi1.getText().isEmpty()
                        && !tfPhone1.getText().isEmpty()
                        ) {
                    //them vao obsevablelist
                    BenhNhan benhnhan = new BenhNhan();
                    benhnhan.setHoTen(tfTen1.getText());
                    benhnhan.setNgaySinh(date1.getValue().toString());
                    benhnhan.setGioiTinh(gioitinh1);
                    benhnhan.setDiaChi(tfDiaChi1.getText());
                    benhnhan.setPhone(tfPhone1.getText());
                    benhnhan.setTrangThai("phòng khám");
                    //Them thong tin benh nhan vao co so du lieu
                    String sql = "insert into Benh_Nhan values (NULL,?,?,?,?,?,?);";
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
                    PreparedStatement ps2 = con.getPS("INSERT INTO Phien_Kham(Ma_Benh_Nhan, Thoi_Gian_Kham) VALUES (?,?)");
                    ps2.setInt(1, benhnhan.getMa());
                    long timeNow = Calendar.getInstance().getTimeInMillis();
                    Timestamp ts = new java.sql.Timestamp(timeNow);
                    ps2.setTimestamp(2, ts);
                    ps2.executeUpdate();
                    benhnhan.setThoiGian(ts.toString());
                    //them benh nhan moi nhat vao dau bang
                    //Can xem lai
                    BenhNhanData.add(0, benhnhan);
                    refreshTabThemMoi();    
                }
                else{
                    Alert alert2 =new Alert(Alert.AlertType.ERROR , "Thiếu Thông Tin" , ButtonType.OK);
                    alert2.showAndWait();
                }
                
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void refreshTabThemMoi(){
        tfTen1.setText("");
        date1.setValue(null);
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
            Alert alert = new Alert(AlertType.CONFIRMATION, "Xác nhận sửa thông tin?" , ButtonType.YES , ButtonType.NO);
            alert.setTitle("Xác Nhận");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES){
                if (!tfTen2.getText().isEmpty()
                        && !date2.getValue().toString().isEmpty()
                        && !gioitinh2.isEmpty()
                        && !tfDiaChi2.getText().isEmpty()
                        && !tfPhone2.getText().isEmpty()
                        ) {
                    //cap nhat vao du lieu
                    String sql = "Update Benh_Nhan set Ho_Ten = ?, Ngay_Sinh = ? , Dia_Chi = ? , Gioi_Tinh = ?, SDT_BN = ? where Ma_Benh_Nhan = ?";
                    ps = con.getPS(sql);
                    //ngaysinh = "" + cbNam2.getValue() + "-" + cbThang2.getValue() + "-" +cbNgay2.getValue();
                    ps.setString(1, tfTen2.getText());
                    ps.setString(2, date2.getValue().toString());
                    ps.setString(3, tfDiaChi2.getText());
                    ps.setString(4, gioitinh2);
                    ps.setString(5, tfPhone2.getText());
                    ps.setInt(6, bnSelected.getMa());
                    ps.executeUpdate();
                    ps.close();

                    //cap nhat vao bang
                    bnSelected.setHoTen(tfTen2.getText());
                    bnSelected.setNgaySinh(date2.getValue().toString());
                    bnSelected.setDiaChi(tfDiaChi2.getText());
                    bnSelected.setGioiTinh(gioitinh2);
                    bnSelected.setPhone(tfPhone2.getText());
                    int index = BenhNhanData.indexOf(bnSelected);
                    BenhNhanData.set(index, bnSelected);
                }
                else{
                    Alert alert2 =new Alert(Alert.AlertType.ERROR , "Thiếu Thông Tin" , ButtonType.OK);
                    alert2.showAndWait();
                }
            }
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void Xoa(ActionEvent e) throws IOException{
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Xác nhận xóa bệnh nhân?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Xác Nhận");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES){
                String sql = "delete from Benh_Nhan where Ma_Benh_Nhan = ?";
                ps = con.getPS(sql);
                ps.setInt(1, bnSelected.getMa());
                ps.executeUpdate();
                ps.close();
                BenhNhanData.remove(bnSelected);
                bnSelected = null;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
