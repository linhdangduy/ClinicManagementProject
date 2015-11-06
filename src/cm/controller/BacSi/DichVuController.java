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
import javafx.scene.layout.HBox;

/**
 *
 * @author linhsan
 */
public class DichVuController implements Initializable, PaneInterface {
    
    private BacSiController parentPane;
    
    @FXML
    private void handleBtnTroLai(ActionEvent event) {
        parentPane.setPane("tiepnhan");
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
