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
public class ThuocController implements Initializable {

    @FXML
    private static HBox Thuoc;
    
    public static HBox get() {
        return Thuoc;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}