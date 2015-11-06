/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
/**
 *
 * @author linhsan
 */
public class TiepNhanController implements Initializable, PaneInterface {
    
    @FXML
    private Button btnChiTiet;
    @FXML
    private VBox boxDanhSachBN;
    @FXML
    private TabPane tabDieuTri;
    
    private BacSiController parentPane;
    
    @FXML
    private void handleBtnChiTiet(ActionEvent event) {
        tabDieuTri.setVisible(true);
        boxDanhSachBN.setVisible(false);
    }
    @FXML
    private void handleBtnTroLai(ActionEvent event) {
        tabDieuTri.setVisible(false);
        boxDanhSachBN.setVisible(true);
    }
    
    @FXML
    private void handleBtnKeDon(ActionEvent event) {
        parentPane.setPane("thuoc");
    }
    @FXML
    private void handleBtnSuDung(ActionEvent event) {
        parentPane.setPane("dichvu");
    }
    
    //override tu Initializable interface
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    //override tu PaneInterface interface
    @Override
    public void setScreenParent(BacSiController mainPane) {
        parentPane = mainPane;
    }
    
}
