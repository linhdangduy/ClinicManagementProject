/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.DuocSi;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author linhsan
 */
public class ThuocController implements Initializable {
    @FXML
    private Label lblTenThuoc;
    @FXML
    private Label lblCongDung;
    @FXML
    private Label lblDonVi;
    @FXML
    private Label lblGiaThuoc;
    @FXML
    private Label lblTrangThai;
    @FXML
    private TextField tfTenThuoc;
    @FXML
    private TextField tfGiaThuoc;
    @FXML
    private TextField tfTrangThai;
    @FXML
    private ComboBox<?> cbDonVi;
    @FXML
    private TextField tfTenThuoc1;
    @FXML
    private TextField tfGiaThuoc1;
    @FXML
    private TextField tfTrangThai1;
    @FXML
    private ComboBox<?> cbDonVi1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void Them(ActionEvent event) {
    }

    @FXML
    private void Huy(ActionEvent event) {
    }

    @FXML
    private void Sua(ActionEvent event) {
    }

    @FXML
    private void Xoa(ActionEvent event) {
    }
    
}
