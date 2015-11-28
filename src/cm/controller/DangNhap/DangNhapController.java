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
    private ResultSet rs;
    private PreparedStatement ps;
    private static String employeeName;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {        
        con = new ConnectToDatabase();
        lbThongBao.setText("");
    }
    
    public static String getEmployeeName() {
        return employeeName;
    }
    
    @FXML
    private void dangnhap(ActionEvent e) {
        String sql = "SELECT Ten_Nhan_Vien, Phong FROM Tai_Khoan WHERE Ten_Dang_Nhap = ? AND Mat_Khau = ?";
        try {
            ps = con.getPS(sql);
            String name=tfName.getText();
            ps.setString(1, name);
            ps.setString(2, tfPass.getText());
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                rs.next();
                employeeName = rs.getString("Ten_Nhan_Vien");
                String phong = rs.getString("Phong");
                if (phong.equals("phòng khám")){
                    setView("/cm/view/BacSi/BacSi.fxml");
                }
                else if (phong.equals("lễ tân")){
                    setView("/cm/view/LeTan/LeTan.fxml");
                }
                else if (phong.equals("phòng thuốc")) {
                    setView("/cm/view/DuocSi/DuocSi.fxml");
                }
                else if (phong.equals("admin")) {
                    setView("/cm/view/QuanLy/QuanLy.fxml");
                }
                con.conClose();
            }
            else{
                lbThongBao.setText("Tên tài khoản hoặc mật khẩu sai!");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    private void handleBtnDangKy(ActionEvent e) {
        setView("/cm/view/DangNhap/DangKy.fxml");
    }
    
    private void setView(String url) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(url));
            Scene scene = new Scene(root);
            ClinicManager.getStage().setScene(scene);
        } catch (IOException ex) {
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
