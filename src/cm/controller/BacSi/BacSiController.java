/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

import cm.ClinicManager;
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
public class BacSiController implements Initializable {
    
    @FXML
    private StackPane StackPane;
    @FXML
    private Label lblDangXuat;
    
    private HashMap<String, HBox> screens = new HashMap<>();
    
    private void loadPane(String paneName, String resource) {
        try {
            FXMLLoader load = new FXMLLoader(getClass().getResource(resource));
            HBox pane = load.load();
            PaneInterface paneController = (PaneInterface)load.getController();
            paneController.setScreenParent(this);
            screens.put(paneName, pane);           
        } catch (IOException ex) {
            Logger.getLogger(BacSiController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setPane1(String pane, boolean b) {
        if (!StackPane.getChildren().isEmpty()) {
            StackPane.getChildren().remove(0);
        }
        StackPane.getChildren().add(0, screens.get(pane));
    }
    
    public void setPane(String pane) {
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
    
    @FXML
    private void mouseEnteredLblDangXuat(MouseEvent event) {
        
    }
    @FXML
    private void mouseExitedLblDangXuat(MouseEvent event) {
        
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPane("tiepnhan", "/cm/view/BacSi/TiepNhan.fxml");
        loadPane("thuoc", "/cm/view/BacSi/Thuoc.fxml");
        loadPane("dichvu", "/cm/view/BacSi/DichVu.fxml");
        setPane("tiepnhan");
    }

    
}
