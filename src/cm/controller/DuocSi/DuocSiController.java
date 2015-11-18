/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.DuocSi;

import cm.ClinicManager;
import cm.controller.BacSi.BacSiController;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author linhsan
 */
public class DuocSiController implements Initializable {

    @FXML
    private StackPane StackPane;
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
        setPane("tiepnhan");
    }
    
}
