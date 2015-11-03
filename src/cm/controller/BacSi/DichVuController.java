/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

/**
 *
 * @author linhsan
 */
public class DichVuController implements Initializable {
    
    @FXML
    private static HBox DichVu;
    
    public static void setVisible(boolean b) {
        DichVu.setVisible(b);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
