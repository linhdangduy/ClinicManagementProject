/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

import cm.ConnectToDatabase;
import cm.model.Dichvu;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.beans.binding.Bindings.size;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author vincent
 */
public class DichVuController implements Initializable, PaneInterface {

    private BacSiController parentPane;
    private ConnectToDatabase con;
    private PreparedStatement ps;
    private ResultSet rs;
    @FXML
    private TableView<Dichvu> DichvuTable;
    @FXML
    private TableColumn ColDichvuMa;
    @FXML
    private TableColumn ColDichvuTen;
    @FXML
    private TableColumn ColDichvuChucnang;
    @FXML
    private TableColumn ColDichvuGia;
    @FXML
    private ComboBox<String> cbLoc;
    @FXML
    private Label lblTenDichVu;
    @FXML
    private TextArea taChucNang;
    @FXML
    private TextArea taThem;
    @FXML
    private Label lblGia;
    @FXML
    private TextField tfLoc;
    @FXML
    private VBox paneThemDichVu;
    private ObservableList<Dichvu> DichvuData = FXCollections.observableArrayList();
    private ObservableList<Dichvu> KeDonDichvuData = FXCollections.observableArrayList();
    private int Ma;
    private float Giaf;
    private String Gia;
    private String Them = "";
    private String S = "";
    private int[] arrayInt = new int[100];
    private int i = 0;
    private int n = 0;
    private int flag = 0;

    /*
     Visible when press button 'Ke don thuoc' in TiepNhan.
     when button 'Thuoc' is pressed, it is unvisible
     */
    public void setPaneThemDichVu(boolean b) {
        paneThemDichVu.setVisible(b);
    }

    //override tu Initializable interface

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSearchInit();
        addDichVuData();
        initTable(DichvuData);
        ControllerMediator.getInstance().setDichVuCtrl(this);
    }

    @FXML
    private void handleBtnThem(ActionEvent event) {
        Dichvu dichvu = new Dichvu();
        dichvu.setMa(Ma);
        dichvu.setTenDichVu(lblTenDichVu.getText());
        dichvu.setChucNang(taChucNang.getText());
        dichvu.setGia(Giaf);
        flag = 0;
        for (i = 0; i <= n; i++) {
            if (arrayInt[i] == Ma) {
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            n++;
            arrayInt[n] = Ma;
            KeDonDichvuData.add(dichvu);
            Them = S.concat(lblTenDichVu.getText());
            S = Them.concat(", ");
            taThem.setText(Them);
        }

        // DichvuTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> addDichvu(newValue);
    }

    @FXML
    private void handleBtnTroLai(ActionEvent event) {
        initTable(DichvuData);
        Them="";
        S="";
        for(i=0;i<=n;i++)
        {
            arrayInt[i]=0;
        }
        taChucNang.setText("");
        taThem.setText("");
        parentPane.setPane("tiepnhan");
    }

    @FXML
    private void handleBtnXong(ActionEvent event) throws SQLException {
        Alert dialogStage = new Alert(AlertType.CONFIRMATION);
        dialogStage.setTitle("XÁC NHẬN");
        dialogStage.setHeaderText("Bạn có muốn thêm dịch vụ?");
        Optional<ButtonType> result = dialogStage.showAndWait();
        if (result.get() == ButtonType.OK) {
            initTable(KeDonDichvuData);
        } else {
            KeDonDichvuData.clear();
            initTable(DichvuData);
        }
    }

    private void addDichVuData() {
        try {
            con = new ConnectToDatabase();
            String sql = "SELECT* FROM Dich_Vu ORDER BY Ma_Dich_Vu ASC";
            rs = con.getRS(sql);
            while (rs.next()) {
                Dichvu dichvu = new Dichvu();
                dichvu.setMa(rs.getInt("Ma_Dich_Vu"));
                dichvu.setTenDichVu(rs.getString("Ten_Dich_Vu"));
                dichvu.setChucNang(rs.getString("Chuc_Nang"));
                dichvu.setGia(rs.getFloat("Gia_Dich_Vu"));
                DichvuData.add(dichvu);
                System.out.println(DichvuData.size());
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DichVuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cbSearchInit() {
        cbLoc.getItems().addAll("Mã", "Tên Dịch Vụ", "Giá");
        cbLoc.setValue("Mã");
    }

    public void initTable(ObservableList<Dichvu> Data) {
        ColDichvuMa.setCellValueFactory(new PropertyValueFactory<>("ma"));
        ColDichvuTen.setCellValueFactory(new PropertyValueFactory<>("tenDichVu"));
        ColDichvuChucnang.setCellValueFactory(new PropertyValueFactory<>("chucNang"));
        ColDichvuGia.setCellValueFactory(new PropertyValueFactory<>("gia"));

        FilteredList<Dichvu> filteredData = new FilteredList<>(Data, p -> true);
        DichvuTable.setItems(filteredData);
        tfLoc.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(dichvu -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (cbLoc.getValue()) {
                    case "Mã":
                        if (dichvu.getMa() == Integer.parseInt(newValue)) {
                            return true;
                        }
                        break;
                    case "Tên Dịch Vụ":
                        if (dichvu.getTenDichVu().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Giá":
                        if (dichvu.getGia() == Float.parseFloat(newValue)) {
                            return true;
                        }
                }
                return false;
            });
        });
        DichvuTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showDetails(newValue));
    }

    private void showDetails(Dichvu dichvu) {
        if (dichvu != null) {
            Ma = dichvu.getMa();
            lblTenDichVu.setText(dichvu.getTenDichVu());
            Giaf = dichvu.getGia();
            Gia = Float.toString(dichvu.getGia());
            taChucNang.setText(dichvu.getChucNang());
            lblGia.setText(Gia);
        } else {
            lblTenDichVu.setText("");
            lblGia.setText("");
        }
    }

    //override tu PaneInterface interface
    @Override
    public void setScreenParent(BacSiController mainPane) {
        parentPane = mainPane;
    }

}
