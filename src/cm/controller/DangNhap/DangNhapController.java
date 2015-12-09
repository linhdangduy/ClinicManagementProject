/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.DangNhap;

import cm.ClinicManager;
import cm.ConnectToServer;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.sql.rowset.CachedRowSet;

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
    private ResultSet rs;
    private PreparedStatement ps;
    private static String employeeName;
    private static String tenDangNhap;
    private ConnectToServer con;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {        
        lbThongBao.setText("");
    }
    
    public static String getTenDangNhap() {
        return tenDangNhap;
    }
    
    public static String getEmployeeName() {
        return employeeName;
    }
    
    @FXML
    private void dangnhap(ActionEvent e) throws SQLException {
        String name = tfName.getText();
        String pass = tfPass.getText();
        if (!name.equals("") && !pass.equals("")) {
            //construct query
            String query = "SELECT Ten_Nhan_Vien, Phong, Trang_Thai FROM Tai_Khoan "
                + "WHERE Ten_Dang_Nhap = '" + name 
                + "' AND Mat_Khau = '" + pass +"'";
            con = new ConnectToServer();
            con.sendToServer(query);
            Object ob = con.receiveFromServer();
                /*
                 * if returned object is not CacheRowSet, inform and end the loop
                 * else use CacheRowSet and end the loop
                */
            if (ob.getClass().toString().equals("class java.lang.String")) {
                lbThongBao.setText("Tên tài khoản hoặc mật khẩu sai!");
            }
            else {
                tenDangNhap = name;
                CachedRowSet crs = (CachedRowSet)ob;
                crs.next();
                String trangThai = crs.getString("Trang_Thai");
                if (trangThai.equals("Nghỉ")) {
                    employeeName = crs.getString("Ten_Nhan_Vien");
                    String phong = crs.getString("Phong");

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
                    query = "UPDATE Tai_Khoan SET Trang_Thai = 'Hoạt động' WHERE Ten_Dang_Nhap =  '"
                                + tenDangNhap +"'";
                    con.sendToServer(query);
                    con.sendToServer("done");
                }
                else if (trangThai.equals("Hoạt động"))
                    lbThongBao.setText("tài khoản đang hoạt động");
                else if (trangThai.equals("Đăng ký"))
                    lbThongBao.setText("tài khoản đang chờ được chấp nhân");
            }
        }
        else {
            lbThongBao.setText("Tên tài khoản hoặc mật khẩu sai!");
        }
    }
    
    @FXML
    private void handleBtnDangKy(ActionEvent e) {
        System.out.println("linh");
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
        Platform.exit();
    }
    
    
}
