/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.DangNhap;

import cm.ClinicManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author linhsan
 */
public class DangKyController implements Initializable {
    @FXML
    private TextField tfTenDangNhap;
    @FXML
    private TextField tfDiaChi;
    @FXML
    private TextField tfSoDienThoai;
    @FXML
    private RadioButton rbNam;
    @FXML
    private ToggleGroup groupBtn;
    @FXML
    private RadioButton rbNu;
    @FXML
    private ComboBox<?> cbBacHoc;
    @FXML
    private TextField tfTenNhanVien;
    @FXML
    private TextField tfNganh;
    @FXML
    private PasswordField tfMatKhau;
    @FXML
    private ComboBox<?> cbPhong;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
