/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.DangNhap;

import cm.ClinicManager;
import cm.ConnectToDatabase;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author linhsan
 */
public class DangKyController implements Initializable {
    ConnectToDatabase con;
    private ResultSet rs;
    private PreparedStatement ps;
    @FXML
    private GridPane pane;
    @FXML
    private TextField tfTenDangNhap;
    @FXML
    private TextField tfDiaChi;
    @FXML
    private RadioButton rbNam;
    @FXML
    private ToggleGroup groupBtn;
    @FXML
    private RadioButton rbNu;
    @FXML
    private ComboBox<String> cbBacHoc;
    @FXML
    private TextField tfNganh;
    @FXML
    private PasswordField tfMatKhau;
    @FXML
    private ComboBox<String> cbPhong;
    @FXML
    private TextField tfPhone;
    @FXML
    private CheckBox ChBox;
    @FXML
    private TextField tfHoTen;
    @FXML
    private DatePicker ngaysinh;
    @FXML
    private Label lblTrangThai;
    final TextField textField =  new TextField();
    ToggleGroup group = new ToggleGroup();
    String gioitinh = "Nam";
    int statusTDN = 1, statusDT = 1, statusMK = 1, statusDC = 1, statusNganh = 1, statusHT = 1, statusNS = 1;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = new ConnectToDatabase();
        setListen();
        cbBacHocinit();
        cbPhonginit();
        CheckBoxAct();
        radiobtn();
        checkStatus();
        checkTenDN();
        CheckSDT();
    }    
    
    private void radiobtn(){
        rbNam.setToggleGroup(group);
        rbNu.setToggleGroup(group);
        rbNam.setSelected(true);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                gioitinh = ((Labeled)newValue).getText();
            }
        });
        
        
    }
    private void checkTenDN(){
        String sql = "SELECT Ten_Dang_Nhap FROM Tai_Khoan WHERE Ten_Dang_Nhap = ?";
        tfTenDangNhap.textProperty().addListener(new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if(newValue.length()>20)
                    {
                        lblTrangThai.setText("* Tên đăng nhập tối đa 20 ký tự");
                        tfTenDangNhap.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
                        statusTDN = 1;
                    }
                    else if(newValue.length()<5)
                    {
                        lblTrangThai.setText("* Tên đăng nhập tối thiểu 5 ký tự");
                        tfTenDangNhap.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
                        statusTDN = 1;
                    }
                    else
                    {
                        checkStatus();
                        ps = con.getPS(sql);
                        ps.setString(1, newValue);
                        rs = ps.executeQuery();
                        if(rs.isBeforeFirst())
                        {
                            lblTrangThai.setText("* Tên đăng nhập đã tồn tại");
                            tfTenDangNhap.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
                            statusTDN = 1;
                        }
                        else{
                            statusTDN = 0;
                            checkStatus();
                        }
                        ps.close();
                        rs.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DangKyController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
    }
    
    private void CheckSDT(){
        tfPhone.textProperty().addListener(new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                
                if(newValue.length()>12)
                    {
                        lblTrangThai.setText("* Số điện thoại tối đa 12 ký tự");
                        tfTenDangNhap.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
                        statusDT = 1;
                    }
                else
                for(int i=0;i<newValue.length();i++)
                {
                    if(newValue.charAt(i)=='+' || (newValue.charAt(i)>='0' && newValue.charAt(i)<='9' ))
                    {   
                        if(i == newValue.length() -1)
                        {
                            statusDT = 0;
                            checkStatus();
                            break;
                        }
                    }
                    else {
                        statusDT = 1;
                        tfPhone.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
                        lblTrangThai.setText("* Số điện thoại chỉ gồm chữ số và dấu '+'");
                        break;
                    }
                }
            }
            
        });
    }
    
    private void setListen(){
        tfHoTen.textProperty().addListener((v, oldValue, newValue)-> {
            if(newValue.length()>30)
                    {
                        lblTrangThai.setText("* Họ tên tối đa 30 ký tự");
                        tfHoTen.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
                        statusHT = 1;
                    }
            else if(newValue.isEmpty())
            {
                statusHT = 1;
                checkStatus();
            }
            else
            {    
                statusHT = 0;
                checkStatus();
            }
        });
        
        tfMatKhau.textProperty().addListener((e, oldValue, newValue)->{
            if(newValue.length()>15)
                    {
                        lblTrangThai.setText("* Mật khẩu tối đa 15 ký tự");
                        tfMatKhau.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
                        statusMK = 1;
                    }
            else if(newValue.isEmpty())
            {
                statusMK = 1;
                checkStatus();
            }
            else
            {
                statusMK = 0;
                checkStatus();
            }
        });
        textField.textProperty().addListener((e, oldValue, newValue)->{
            if(newValue.length()>15)
                    {
                        lblTrangThai.setText("* Mật khẩu tối đa 15 ký tự");
                        textField.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
                        statusDT = 1;
                    }
            else if(newValue.isEmpty())
            {
                statusMK = 1;
                checkStatus();
            }
            else
            {
                statusMK = 0;
                checkStatus();
            }
        });
        ngaysinh.valueProperty().addListener((e, oldValue, newValue)->{
            if(newValue.toString().isEmpty())
                statusNS = 1;
            else statusNS = 0;
            checkStatus();
        });
        tfDiaChi.textProperty().addListener((e, oldValue, newValue)->{
            if(newValue.isEmpty())
                statusDC = 1;
            else statusDC = 0;
            checkStatus();
        });
        tfNganh.textProperty().addListener((e, oldValue, newValue)->{
            if(newValue.length()>15)
                    {
                        lblTrangThai.setText("* Chuyên ngành tối đa 20 ký tự");
                        tfNganh.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
                        statusNganh = 1;
                    }
            else if(newValue.isEmpty())
            {
                statusNganh = 1;
                checkStatus();
            }
            else
            {
                statusNganh = 0;
                checkStatus();
            }
        });
        
    }
    
    private int checkStatus(){
        int status = 0;
        if(statusTDN == 1)
            tfTenDangNhap.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
        else tfTenDangNhap.setStyle(null);
        if(statusHT == 1)
            tfHoTen.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
        else tfHoTen.setStyle(null);
        if(statusMK == 1)
        {
            tfMatKhau.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
            textField.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
        }
        else
        {
            tfMatKhau.setStyle(null);
            textField.setStyle(null);
        }
        if(statusNS == 1)
            ngaysinh.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
        else ngaysinh.setStyle(null);
        if(statusDC == 1)
            tfDiaChi.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
        else tfDiaChi.setStyle(null);
        if(statusNganh == 1)
            tfNganh.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
        else tfNganh.setStyle(null);
        if(statusDT == 1)
            tfPhone.setStyle("-fx-border-color: #ff0000;"+"-fx-border-width: 2");
        else tfPhone.setStyle(null);
        status = statusDT + statusDC + statusHT + statusMK + statusNS + statusNganh + statusTDN;
        if(status == 0)
            lblTrangThai.setText(null);
        else lblTrangThai.setText("* Điền đầy đủ thông tin không bỏ trống");
    
        return status;
    }
    
    private void CheckBoxAct(){
        textField.setPrefSize(275, 35);
        
        textField.setFont(new Font(13));
       // textField.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
       // textField.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        textField.setLayoutX(275);
        textField.setLayoutY(35);
        textField.setManaged(false);
        textField.setVisible(false);
        GridPane.setConstraints(textField, 1, 2);
        pane.getChildren().add(textField);
        textField.managedProperty().bind(ChBox.selectedProperty());
        textField.visibleProperty().bind(ChBox.selectedProperty());
        tfMatKhau.managedProperty().bind(ChBox.selectedProperty().not());
        tfMatKhau.visibleProperty().bind(ChBox.selectedProperty().not());
        textField.textProperty().bindBidirectional(tfMatKhau.textProperty());
        
    }
    private void cbBacHocinit(){
        cbBacHoc.getItems().addAll("Thạc Sĩ","Tiến Sĩ","Cao Đẳng","Đại Học");
        cbBacHoc.setValue("Đại Học");
    }
    private void cbPhonginit(){
        cbPhong.getItems().addAll("Phòng Khám","Lễ Tân","Phòng Thuốc","Quản Lý");
        cbPhong.setValue("Lễ Tân");
    }
    @FXML
    private void handleBtnDangKy(ActionEvent event) throws SQLException {
        String sql = "INSERT INTO Tai_Khoan VALUES (?,?,?,?,?,?,?,?,?,?,'Đăng Ký');";
        if(checkStatus()>0)
            return;
        else
        {
            ps = con.getPS(sql);
            ps.setString(1,tfTenDangNhap.getText());
            ps.setString(2,tfMatKhau.getText());
            ps.setString(3,tfHoTen.getText());
            ps.setString(4,ngaysinh.getValue().toString());
            ps.setString(5,tfDiaChi.getText());
            ps.setString(6,gioitinh);
            ps.setString(7, tfPhone.getText());
            ps.setString(8, tfNganh.getText());
            ps.setString(9, cbBacHoc.getValue());
            switch (cbPhong.getValue()) {
                case "Phòng Khám":
                    ps.setString(10, "phòng khám");
                    break;
                case "Lễ Tân":
                    ps.setString(10,"lễ tân");
                    break;
                case "Phòng Thuốc":
                    ps.setString(10,"phòng thuốc");
                    break;
                case "Quản Lý":
                    ps.setString(10, "admin");
                    break;
            }
            ps.executeUpdate();
            ps.close();
            Alert alert =new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông Báo");
            alert.setHeaderText(null);
            alert.setContentText("Bạn đã gửi đăng ký tài khoản, xin vui lòng chờ Quản trị viên chấp nhận.");
            alert.showAndWait();
            try {
            Parent root = FXMLLoader.load(getClass().getResource("/cm/view/DangNhap/DangNhap.fxml"));
            Scene scene = new Scene(root);
            ClinicManager.getStage().setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(DangKyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }

    @FXML
    private void handleBtnHuy(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/cm/view/DangNhap/DangNhap.fxml"));
            Scene scene = new Scene(root);
            ClinicManager.getStage().setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(DangKyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
