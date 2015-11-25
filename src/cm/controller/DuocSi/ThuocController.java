/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.DuocSi;

import cm.ConnectToDatabase;
import cm.model.Thuoc;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private Label lblSoLuong;
    @FXML
    private Label lblGhiChu;
    @FXML
    private TextField tfTenThuoc;
    @FXML
    private TextArea tfCongDung;
    @FXML
    private TextField tfGiaThuoc;
    @FXML
    private TextField tfSoLuong;
    @FXML
    private ComboBox<String> cbDonVi;
    @FXML
    private TextArea tfGhiChu;
    @FXML
    private TextField tfTenThuoc1;
    @FXML
    private TextField tfGiaThuoc1;
    @FXML
    private TextField tfSoLuong1;
    @FXML
    private ComboBox<String> cbDonVi1;
    @FXML
    private TextArea tfGhiChu1;
    @FXML
    private TextArea tfCongDung1;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private TextField tfFilter;
    @FXML
    private TableView<Thuoc> ThuocTable;
    @FXML
    private TableColumn colMa;
    @FXML
    private TableColumn colTenThuoc;
    @FXML
    private TableColumn colCongDung;
    @FXML
    private TableColumn colDonVi;
    @FXML
    private TableColumn colGiaThuoc;
    @FXML
    private TableColumn colSoLuong;
    
    private ObservableList<Thuoc> ThuocData = FXCollections.observableArrayList();
    private Thuoc thuocSelected;
    
    private ConnectToDatabase con;
    private PreparedStatement ps;
    private ResultSet rs;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con = new ConnectToDatabase();
            addThuocData();
            initTable();
            cbDonViInit();
            cbSearchInit();
        } catch (SQLException ex) {
            Logger.getLogger(ThuocController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cbSearchInit (){
        cbSearch.getItems().addAll("Mã" , "Tên Thuốc" , "Công Dụng");
        cbSearch.setValue("Tên Thuốc");
    }
    
    private void cbDonViInit() {
        cbDonVi.getItems().addAll("viên" , "vỉ" , "hộp" , "lọ");
        cbDonVi.setValue("Đơn Vị");
        
        cbDonVi1.getItems().addAll("viên" , "vỉ" , "hộp" , "lọ");
        cbDonVi1.setValue("Đơn Vị");
        
    }
    private void addThuocData() throws SQLException{
        String sql = "select * from Thuoc";
        ThuocData.clear();
        rs = con.getRS(sql);
        while (rs.next()){
            Thuoc thuoc = new Thuoc();
            thuoc.setMa(rs.getInt("Ma_Thuoc"));
            thuoc.setTenThuoc(rs.getString("Ten_Thuoc"));
            thuoc.setCongDung(rs.getString("Cong_Dung"));
            thuoc.setDonVi(rs.getString("Don_Vi"));
            thuoc.setGiaThuoc(rs.getFloat("Gia_Thuoc"));
            thuoc.setsoLuong(rs.getInt("So_Luong"));
            thuoc.setGhiChu(rs.getString("Ghi_Chu_Thuoc"));
            ThuocData.add(thuoc);
        }
        rs.close();
    }
    private void initTable(){
        colMa.setCellValueFactory(new PropertyValueFactory<>("ma"));
        colTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        colCongDung.setCellValueFactory(new PropertyValueFactory<>("congDung"));
        colDonVi.setCellValueFactory(new PropertyValueFactory<>("donVi"));
        colGiaThuoc.setCellValueFactory(new PropertyValueFactory<>("giaThuoc"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        
        FilteredList<Thuoc> filteredData = new FilteredList<>(ThuocData, p -> true);
        ThuocTable.setItems(filteredData);
        tfFilter.textProperty().addListener((observable, oldValue , newValue) -> {
            filteredData.setPredicate( thuoc -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                switch(cbSearch.getValue()) {
                    case "Mã":
                        if (thuoc.getMa() == Integer.parseInt(newValue)){
                            return true;
                        }
                        break;
                    case "Tên Thuốc":
                        if (thuoc.getTenThuoc().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                    case "Công Dụng":
                        if (thuoc.getCongDung().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        break;
                }
                return false;
            });
        });
       
        ThuocTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));

    }
    private void showDetails(Thuoc thuoc) {
        thuocSelected = thuoc;
         if (thuoc != null){
             lblTenThuoc.setText(thuoc.getTenThuoc());
             lblCongDung.setText(thuoc.getCongDung());
             lblDonVi.setText(thuoc.getDonVi());
             lblGiaThuoc.setText(Float.toString(thuoc.getGiaThuoc()));
             lblSoLuong.setText(Integer.toString(thuoc.getSoLuong()));
             lblGhiChu.setText(thuoc.getGhiChu());
             
             tfTenThuoc1.setText(thuoc.getTenThuoc());
             tfCongDung1.setText(thuoc.getCongDung());
             cbDonVi1.setValue(thuoc.getDonVi());
             tfGiaThuoc1.setText(Float.toString(thuoc.getGiaThuoc()));
             tfSoLuong1.setText(Integer.toString(thuoc.getSoLuong()));
             tfGhiChu1.setText(thuoc.getGhiChu());
             
         }
         else{
             lblTenThuoc.setText(null);
             lblCongDung.setText(null);
             lblDonVi.setText(null);
             lblGiaThuoc.setText(null);
             lblSoLuong.setText(null);
             lblGhiChu.setText(null);
             
             tfTenThuoc1.setText(null);
             tfCongDung1.setText(null);
             cbDonVi1.setValue("Đơn Vị");
             tfGiaThuoc1.setText(null);
             tfSoLuong1.setText(null);
             tfGhiChu1.setText(null);
         }
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
