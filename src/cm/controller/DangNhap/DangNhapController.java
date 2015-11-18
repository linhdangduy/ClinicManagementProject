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
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author linhsan
 */
public class DangNhapController implements Initializable {
    @FXML
    private TextField tfName;
    @FXML
    private PasswordField tfPass;
    @FXML
    private Label lbThongBao;
    private ConnectToDatabase con;
    private Statement st;
    private ResultSet rs;
    private PreparedStatement ps;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {        
        con = new ConnectToDatabase();
        lbThongBao.setText("");
    }
    @FXML
    private void dangnhap(ActionEvent e) throws IOException {
        String sql = "SELECT * FROM Tai_Khoan WHERE Ten_Dang_Nhap = ? AND Mat_Khau = ?";
        try {
            ps = con.getPS(sql);
            String name=tfName.getText();
            ps.setString(1, name);
            ps.setString(2, tfPass.getText());
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                rs.next();
                lbThongBao.setText("Thanh cong!");
                if (rs.getString("Phong").equals("phòng khám")){
                    Parent root = FXMLLoader.load(getClass().getResource("/cm/view/BacSi/BacSi.fxml"));
                    Scene scene = new Scene(root);
                    ClinicManager.getStage().setScene(scene);
                }
                else if (rs.getString("Phong").equals("lễ tân")){
                    Parent root = FXMLLoader.load(getClass().getResource("/cm/view/LeTan/LeTan.fxml"));
                    Scene scene = new Scene(root);
                    ClinicManager.getStage().setScene(scene);
                }
                con.conClose();
            }
            else{
                lbThongBao.setText("Ten tai khoan hoac mat khau sai!");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @FXML
    private void thoat(ActionEvent e){
        try {
            con.conClose();
            Platform.exit();
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
