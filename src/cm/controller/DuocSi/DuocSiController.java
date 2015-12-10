/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.DuocSi;

import cm.ClinicManager;
import cm.ConnectToServer;
import cm.controller.BacSi.BacSiController;
import cm.controller.DangNhap.DangNhapController;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author linhsan
 */
public class DuocSiController implements Initializable {
    @FXML
    private HBox hbox;
    @FXML
    private HBox hbox1;
    @FXML
    private StackPane StackPane;
    @FXML
    private Label lblTenDuocsi;
    private HashMap<String, HBox> screens = new HashMap<>();
    
    private void loadPane() {
        try {
            FXMLLoader load1 = new FXMLLoader(getClass().getResource("/cm/view/DuocSi/TiepNhan.fxml"));
            FXMLLoader load2 = new FXMLLoader(getClass().getResource("/cm/view/DuocSi/Thuoc.fxml"));
            HBox paneTiepNhan = load1.load();
            HBox paneThuoc = load2.load();
            screens.put("tiepnhan", paneTiepNhan);
            screens.put("thuoc", paneThuoc);
            
        } catch (IOException ex) {
            Logger.getLogger(BacSiController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setPane(String pane) {
        if (!StackPane.getChildren().isEmpty()) {
            StackPane.getChildren().remove(0);
        }
        StackPane.getChildren().add(0, screens.get(pane));
    }
    
    @FXML
    private void mouseClickedLblDangXuat(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/cm/view/DangNhap/DangNhap.fxml"));
            Scene scene = new Scene(root);
            ClinicManager.getStage().setScene(scene);
            
            ConnectToServer con = new ConnectToServer();
            String query = "UPDATE Tai_Khoan SET Trang_Thai = 'Nghỉ' WHERE Ten_Dang_Nhap =  '"
                                + DangNhapController.getTenDangNhap() +"'";
            con.sendToServer(query);
            con.sendToServer("done");
        } catch (IOException ex) {
            Logger.getLogger(BacSiController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleBtnTiepNhan() {
        setPane("tiepnhan");
    }
    
    @FXML
    private void handleBtnThuoc() {
        setPane("thuoc");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPane();
        hbox.getStylesheets().add("/cm/view/QuanLy/text.css");
        hbox.setStyle("-fx-background-color: #9ca9f1;");
        hbox1.getStylesheets().add("/cm/view/QuanLy/text.css");
        hbox1.setStyle("-fx-background-color: #9ca9f1;");
        setPane("tiepnhan");
          ClinicManager.getStage().setOnCloseRequest(e -> {
            String sql = "UPDATE Tai_Khoan set Trang_Thai='Nghỉ' where Ten_Dang_Nhap = '" + DangNhapController.getTenDangNhap() + "';";
            ConnectToServer con = new ConnectToServer();
            con.sendToServer(sql);
            con.sendToServer("done");
            
        });
        lblTenDuocsi.setText(DangNhapController.getEmployeeName());
    }
    
}
