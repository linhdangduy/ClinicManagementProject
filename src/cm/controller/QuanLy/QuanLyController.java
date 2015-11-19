package cm.controller.QuanLy;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author linhsan
 */
public class QuanLyController implements Initializable {
    @FXML
    private Label lblDangXuat;
    @FXML
    private Label lblTenDangNhap;
    @FXML
    private Label lblNguoiDung;
    @FXML
    private Label lblNgaySinh;
    @FXML
    private Label lblGioiTinh;
    @FXML
    private Label lblDiaChi;
    @FXML
    private Label lblSoDienThoai;
    @FXML
    private Label lblChuyenNganh;
    @FXML
    private Label lblBacHoc;
    @FXML
    private TextField tfTen1;
    @FXML
    private TextField tfDiaChi1;
    @FXML
    private TextField tfPhone1;
    @FXML
    private ComboBox<?> cbNgay1;
    @FXML
    private ComboBox<?> cbThang1;
    @FXML
    private ComboBox<?> cbNam1;
    @FXML
    private RadioButton rbNam1;
    @FXML
    private ToggleGroup groupLevel1;
    @FXML
    private RadioButton rbNu1;
    @FXML
    private TextField tfTen11;
    @FXML
    private TextField tfTen111;
    @FXML
    private TextField tfDiaChi11;
    @FXML
    private TextField tfPhone11;
    @FXML
    private ComboBox<?> cbNgay11;
    @FXML
    private ComboBox<?> cbThang11;
    @FXML
    private ComboBox<?> cbNam11;
    @FXML
    private RadioButton rbNam11;
    @FXML
    private ToggleGroup groupLevel11;
    @FXML
    private RadioButton rbNu11;
    @FXML
    private TextField tfTen1111;
    @FXML
    private ComboBox<?> cbSearch;
    @FXML
    private TextField tfFilter;
    @FXML
    private TableView<?> BenhNhanTable;
    @FXML
    private TableColumn<?, ?> MaColumn;
    @FXML
    private TableColumn<?, ?> TenColumn;
    @FXML
    private TableColumn<?, ?> NgaySinhColumn;
    @FXML
    private TableColumn<?, ?> GioiTinhColumn;
    @FXML
    private TableColumn<?, ?> PhoneColumn;
    @FXML
    private TableColumn<?, ?> ThoiGianColumn;
    @FXML
    private TableColumn<?, ?> TrangThaiColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void mouseClickedLblDangXuat(MouseEvent event) {
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
