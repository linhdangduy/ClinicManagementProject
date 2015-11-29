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
    ToggleGroup group = new ToggleGroup();
    String gioitinh = "Nam";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = new ConnectToDatabase();
        CheckBoxAct();
        radiobtn();
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
                    if(newValue.length()<5)
                        lblTrangThai.setText("* Tên đăng nhập tối thiểu 5 ký tự");
                    else lblTrangThai.setText("");
                    ps = con.getPS(sql);
                    ps.setString(1, newValue);
                    rs = ps.executeQuery();
                    if(rs.isBeforeFirst())
                        lblTrangThai.setText("* Tên đăng nhập đã tồn tại");
                    else lblTrangThai.setText("");
                    ps.close();
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DangKyController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
    }
    
    private void CheckBoxAct(){
        final TextField textField =  new TextField();
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
    private void handleBtnDangKy(ActionEvent event) {
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
