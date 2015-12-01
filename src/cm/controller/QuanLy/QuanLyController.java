/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.QuanLy;

import cm.controller.DangNhap.DangNhapController;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author linhsan
 */
public class QuanLyController implements Initializable {
    
    @FXML
    private ToggleGroup GroupBtn;
    @FXML
    private Label lblDangXuat;
    @FXML
    private Label lblTenAdmin;
    @FXML
    private Pane mainPane;
    
    /*
    * dùng key là string để chiếu đến các value là view, lưu vào hash map
    */
    private HashMap<String, HBox> screens = new HashMap<>();
    
    /*
    * add các view cần dùng vào trong hashmap
    */
    private void loadPane() {
        try {
            FXMLLoader loadViewTiepNhan = new FXMLLoader(getClass().getResource("/cm/view/QuanLy/TiepNhan.fxml"));
            FXMLLoader loadViewThongKe = new FXMLLoader(getClass().getResource("/cm/view/QuanLy/ThongKe.fxml"));
            HBox hbTiepNhan = loadViewTiepNhan.load();
            HBox hbThongKe = loadViewThongKe.load();
            screens.put("tiepnhan", hbTiepNhan);
            screens.put("thongke", hbThongKe);
            
        } catch (IOException ex) {
            Logger.getLogger(QuanLyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    *set view vào pane chính theo tham số string (key)
    */
    private void setPane(String pane) {
        if (!mainPane.getChildren().isEmpty()) {
            mainPane.getChildren().remove(0);
        }
        mainPane.getChildren().add(0, screens.get(pane));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPane();
        setPane("tiepnhan");
        lblTenAdmin.setText(DangNhapController.getEmployeeName());
    }    

    @FXML
    private void handleBtnTiepNhan(ActionEvent event) {
        //set View tiếp nhận
        setPane("tiepnhan");
    }
    
    @FXML
    private void handleBtnThongKe(ActionEvent event) {
        //set View thống kê
        setPane("thongke");
    }

    @FXML
    private void mouseClickedLblDangXuat(MouseEvent event) {
    }
    
}
