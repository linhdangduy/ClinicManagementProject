/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author linhsan
 */
public class BacSiController implements Initializable {
    
    @FXML
    private ToggleButton btnTiepNhan;
    private ToggleButton btnThuoc;
    private ToggleButton btnDichVu;
    private StackPane StackPane;
    @FXML
    private void handleBtnTiepNhan(ActionEvent event) {
        try {
            HBox n = FXMLLoader.load(getClass().getResource("/cm/view/BacSi/TiepNhan.fxml"));
        //    if(n != null) System.out.println("fdafds");
            StackPane.getChildren().add(n);
        } catch (IOException ex) {
            Logger.getLogger(BacSiController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*    TiepNhanController.setVisible(true);
        ThuocController.setVisible(false);
        DichVuController.setVisible(false);
    */}
    
    @FXML
    private void handleBtnThuoc(ActionEvent event) {
    /*    TiepNhanController.setVisible(false);
        ThuocController.setVisible(true);
        DichVuController.setVisible(false);
    */}
    
    @FXML
    private void handleBtnDichVu(ActionEvent event) {
    /*    TiepNhanController.setVisible(false);
        ThuocController.setVisible(false);
        DichVuController.setVisible(true);
    */}
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
