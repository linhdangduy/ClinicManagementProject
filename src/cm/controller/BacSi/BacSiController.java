/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

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
    private StackPane StackPane;
    
    private HashMap<String, HBox> screens = new HashMap<>();
    
    private void loadPane() {
        try {
            FXMLLoader load1 = new FXMLLoader(getClass().getResource("/cm/view/BacSi/TiepNhan.fxml"));
            FXMLLoader load2 = new FXMLLoader(getClass().getResource("/cm/view/BacSi/Thuoc.fxml"));
            FXMLLoader load3 = new FXMLLoader(getClass().getResource("/cm/view/BacSi/DichVu.fxml"));
            HBox paneTiepNhan = load1.load();
            HBox paneThuoc = load2.load();
            HBox paneDichVu = load3.load();
            screens.put("tiepnhan", paneTiepNhan);
            screens.put("thuoc", paneThuoc);
            screens.put("dichvu", paneDichVu);
            
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
    private void handleBtnTiepNhan(ActionEvent event) {        
        setPane("tiepnhan");
    }
    
    @FXML
    private void handleBtnThuoc(ActionEvent event) {
        setPane("thuoc");
    }
    
    @FXML
    private void handleBtnDichVu(ActionEvent event) {
        setPane("dichvu");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPane();
        setPane("tiepnhan");
    }
    
}
