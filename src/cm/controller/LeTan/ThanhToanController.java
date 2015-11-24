/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.LeTan;

import cm.ConnectToDatabase;
import cm.model.BenhNhan;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author XyzC
 */
public class ThanhToanController implements Initializable {
    
    ConnectToDatabase con;
    private Statement st;
    private PreparedStatement ps;
    private ResultSet rs;
    private float TienThuoc = 0,TienDV = 0,TongTien = 0;
    private  int MaBN;
    
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
        
    
    private ObservableList<BenhNhan> BenhNhanData = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            con = new ConnectToDatabase();
            cbSearchC();
            cbSearchD();
            addBenhNhanData();
            initTable();
        } catch (SQLException ex) {
            Logger.getLogger(ThanhToanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    private void cbSearchC() {
        cbSearch.getItems().addAll("Mã","Họ Tên","Trạng Thái");
        cbSearch.setValue("Họ Tên");
    }
    private void cbSearchD() {
        cbSearchDay.getItems().addAll("Hôm nay","Tất cả");
        cbSearchDay.setValue("Tất cả");
    }
    public void initTable(){
        MaColumn.setCellValueFactory(new PropertyValueFactory<>("Ma"));
        TenColumn.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        NgaySinhColumn.setCellValueFactory(new PropertyValueFactory<>("NgaySinh"));
        GioiTinhColumn.setCellValueFactory(new PropertyValueFactory<>("GioiTinh"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("Thone"));
        ThoiGianColumn.setCellValueFactory(new PropertyValueFactory<>("ThoiGian"));
        TrangThaiColumn.setCellValueFactory(new PropertyValueFactory<>("TrangThai"));
        
        FilteredList<BenhNhan> filteredData = new FilteredList<>(BenhNhanData, p -> true);
        BenhNhanTable.setItems(filteredData);
        SearchText.textProperty().addListener((observable, oldValue , newValue) -> {
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
            String sql2 = "select sum(Chi_Phi_Thuoc) as tient from Don_Thuoc natural join Phien_Kham"
                    +"where Ma_Benh_Nhan = ? and Ma_Phien_Kham not in(select Ma_Phien_Kham from Thanh_Toan) having tient> 0;";
            ps = con.getPS(sql1);
            ps.setInt(1,benhnhan.getMa());
            rs = ps.executeQuery();
            if(rs.isBeforeFirst())
            {
                rs.next();
                TienDV = rs.getFloat("tien");
            }
            else TienDV = 0;
            lblTienDV.setText(Float.toString(TienDV));
  
            ps = con.getPS(sql2);
            ps.setInt(1,benhnhan.getMa());
            rs = ps.executeQuery();
            if(rs.wasNull() == false)
            {
                rs.next();
                TienThuoc = rs.getFloat("tient");
            }
            else TienThuoc = 0;
            
            lblTienThuoc.setText(Float.toString(TienThuoc));            
            TongTien = TienThuoc + TienDV;
            lblTongTien.setText(Float.toString(TongTien));
            
            lblTen.setText(benhnhan.getHoTen());
            lblNgaySinh.setText(benhnhan.getNgaySinh());
            lblGioiTinh.setText(benhnhan.getGioiTinh());
            lblPhone.setText(benhnhan.getPhone());
            lblDiaChi.setText(benhnhan.getDiaChi());
            MaBN = benhnhan.getMa();
            }
    public void addBenhNhanData() throws SQLException{
        String sql = "SELECT * FROM Benh_Nhan natural join (select * from Phien_Kham order by Thoi_Gian_Kham desc) as pk "
                        + "group by Ma_Benh_Nhan "
                        + "order by Thoi_Gian_Kham desc;";
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
    @FXML
    private void ThanhToan(ActionEvent e) throws SQLException {
        if(lblTen.getText() == "" || lblTen.getText() == null)
            return;
        String sql = "insert into Thanh_Toan values (?,?,?,?,?)";
        ps.setInt(1,getMaPK(MaBN));
        //ps.setString(); insert Ten_Dang_Nhap
        ps.setFloat(3,TienThuoc);
        ps.setFloat(4,TienDV);
        ps.setFloat(5,TongTien);
        ps.executeQuery();

        Notice.display(lblTen.getText(), lblNgaySinh.getText(), lblGioiTinh.getText(),
                lblDiaChi.getText(), lblPhone.getText(), lblTienThuoc.getText(),
                lblTienDV.getText(), lblTongTien.getText());
    }
    public int getMaPK(int Ma_Benh_Nhan) throws SQLException{
        String sql = "select Ma_Phien_Kham from Phien_Kham where Ma_Benh_Nhan = ?";
        ps = con.getPS(sql);
        ps.setInt(1, Ma_Benh_Nhan);
        rs = ps.executeQuery();
        if(rs.wasNull())
            return 0;
        else return rs.getInt("Ma_Phien_Kham");
        
    }
        
}
class Notice{
        public static void display(String ht, String ns, String gt, String dc, String sdt,
                                String tienthuoc, String tiendv, String tongt){
            Stage window =  new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Thông Báo");
            window.setWidth(450);
            window.setHeight(270);
            window.setResizable(false);
            
            
            VBox layout1 = new VBox();
            layout1.setPrefWidth(200);
            layout1.setPrefHeight(50);
            layout1.setAlignment(Pos.CENTER);
            Text textlayout = new Text();
            textlayout.setText("Thanh Toán");
            textlayout.setFont(new Font("Arial",20));

            
            GridPane pane = new GridPane();
            Label lblten = new Label("Họ Tên : ");
            lblten.setFont(new Font("Arial",16));
            GridPane.setConstraints(lblten, 0, 0);
            Label lblngays = new Label("Ngày Sinh : ");
            lblngays.setFont(new Font("Arial",16));
            GridPane.setConstraints(lblngays, 0, 1);            
            Label lblgt = new Label("Giới Tính : ");
            GridPane.setConstraints(lblgt, 0, 2);
            lblgt.setFont(new Font("Arial",16));
            Label lbldc = new Label("Địa Chỉ : ");
            GridPane.setConstraints(lbldc, 0, 3);
            lbldc.setFont(new Font("Arial",16));
            Label lblsdt = new Label("Số Điện Thoại : ");
            GridPane.setConstraints(lblsdt, 0, 4);
            lblsdt.setFont(new Font("Arial",16));
            Label lbltienthuoc = new Label("Tiền Thuốc : ");
            GridPane.setConstraints(lbltienthuoc, 0, 5);
            lbltienthuoc.setFont(new Font("Arial",16));
            Label lbltiendv = new Label("Tiền Dịch Vụ : ");
            GridPane.setConstraints(lbltiendv, 0, 6);
            lbltiendv.setFont(new Font("Arial",16));
            Label lbltongtien = new Label("Tổng Tiền : ");
            GridPane.setConstraints(lbltongtien, 0, 7);
            lbltongtien.setFont(new Font("Arial",16));
            Label lbl1 = new Label(ht);
            GridPane.setConstraints(lbl1, 1, 0);
            lbl1.setFont(new Font("Arial",16));
            Label lbl2 = new Label(ns);
            GridPane.setConstraints(lbl2, 1, 1);
            lbl2.setFont(new Font("Arial",16));
            Label lbl3 = new Label(gt);
            GridPane.setConstraints(lbl3, 1, 2);
            lbl3.setFont(new Font("Arial",16));
            Label lbl4 = new Label(dc);
            GridPane.setConstraints(lbl4, 1, 3);
            lbl4.setFont(new Font("Arial",16));
            Label lbl5 = new Label(sdt);
            GridPane.setConstraints(lbl5, 1, 4);
            lbl5.setFont(new Font("Arial",16));
            Label lbl6 = new Label(tienthuoc);
            GridPane.setConstraints(lbl6, 1, 5);
            lbl6.setFont(new Font("Arial",16));
            Label lbl7 = new Label(tiendv);
            GridPane.setConstraints(lbl7, 1, 6);
            lbl7.setFont(new Font("Arial",16));
            Label lbl8 = new Label(tongt);
            GridPane.setConstraints(lbl8, 1, 7);
            lbl8.setFont(new Font("Arial",16));
            pane.getChildren().addAll(lblten,lbl1,lblngays,lbl2,lblgt,lbl3,lbldc,lbl4,lblsdt,lbl5,lbltienthuoc,lbl6,lbltiendv,lbl7,lbltongtien,lbl8);
            pane.setAlignment(Pos.CENTER);
            
            Text notice = new Text("Đã thanh toán!");
            notice.setFont(new Font("Arial",14));
            notice.setFill(Color.RED);
            
            Button button =new Button("OK");
            button.setOnAction(e -> window.close());
            button.setAlignment(Pos.CENTER);
            
            layout1.getChildren().addAll(textlayout,pane,notice,button);
          
            Scene scene = new Scene(layout1,900,700);
            window.setScene(scene);
            window.showAndWait();
            
        }
}