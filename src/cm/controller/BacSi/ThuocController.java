/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

import cm.ConnectToServer;
import cm.model.KeDonThuoc;

import cm.model.Thuoc;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author vincent
 */
public class ThuocController implements Initializable, PaneInterface {

    @FXML
    private Label lblTenThuoc;
    @FXML
    private Label lblDonVi;
    @FXML
    private Label lblGiaThuoc;
    @FXML
    private Label lblSoLuong;

    @FXML
    private TableView<Thuoc> ThuocTable;

    @FXML
    private TableColumn ColThuocMa;
    @FXML
    private TableColumn ColThuocTenthuoc;
    @FXML
    private TableColumn ColThuocGiathuoc;
    @FXML
    private TableColumn ColThuocSoluong;
    @FXML
    private TableColumn ColThuocCongdung;
    @FXML
    private TableColumn ColThuocDonvi;

    @FXML
    private TextArea taCongdung;
    @FXML
    private TextArea taThem;
    @FXML
    private ObservableList<Thuoc> ThuocData = FXCollections.observableArrayList();
    private ObservableList<KeDonThuoc> KeDonThuocData = FXCollections.observableArrayList();

    private TiepNhanController tiepNhanCtrl = ControllerMediator.getInstance().getTiepNhanCtrl();
    Stage dStage = new Stage();
    float Giaf;
    int flag = 0;
    int Flag;
    int n = 0;
    int Ma;
    private String Them = "";
    private String S = "";
    private int[] arrayInt = new int[100];
    private String Gia;
    ConnectToServer con;

    @FXML
    private BacSiController parentPane;
    @FXML
    private ComboBox<String> cbLoc;
    @FXML
    private TextField tfLoc;
    @FXML
    private VBox paneThemThuoc;

    /*
     Visible when press button 'Ke don thuoc' in TiepNhan.
     when button 'Thuoc' is pressed, it is unvisible
     */
    public void setPaneThemThuoc(boolean b) {
        paneThemThuoc.setVisible(b);
    }

    @FXML
    private void handleBtnTroLai(ActionEvent event) throws SQLException {
        //  initTable(ThuocData);
        
        taThem.setText(Them);
        tiepNhanCtrl.themTaThuoc(Them);
        parentPane.setPane("tiepnhan");

    }

    @FXML
    private void handleBtnThem(ActionEvent event) {
        KeDonThuoc thuoc = new KeDonThuoc();
        thuoc.setMa(ThuocTable.getSelectionModel().getSelectedItem().getMa());
        thuoc.setTenThuoc(lblTenThuoc.getText());
        thuoc.setSoLuong(0);
        thuoc.setCachDungThuoc("");
        thuoc.setChiPhiThuoc(0);

        flag = 0;
        for (int i = 0; i <= n; i++) {
            if (arrayInt[i] == Ma) {
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            n++;
            arrayInt[n] = Ma;
            KeDonThuocData.add(thuoc);
            Them = S.concat(lblTenThuoc.getText());
            S = Them.concat(", ");
            taThem.setText(Them);
            tiepNhanCtrl.themTaThuoc(Them);
        }
    }

    @FXML
    private void handleBtnXong(ActionEvent event) throws SQLException, IOException {
        Alert dialogStage = new Alert(AlertType.CONFIRMATION);
        dialogStage.setTitle("Chỉnh Sửa Đơn Thuốc");
        dialogStage.setHeaderText("Bạn có muốn chỉnh sửa Đơn Thuốc");
        //dialogStage.setContentText("Chọn ");

        ButtonType buttonTypeOne = new ButtonType("Sửa Đơn Thuốc");
        ButtonType buttonTypeTwo = new ButtonType("Xóa Đơn Thuốc");
        ButtonType buttonTypeCancel = new ButtonType("Thoát", ButtonData.CANCEL_CLOSE);
        dialogStage.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
        Optional<ButtonType> result = dialogStage.showAndWait();
        if (dialogStage.getResult() == buttonTypeOne) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/cm/view/BacSi/KeDonThuoc.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Scene scene = new Scene(page);
            dStage.setScene(scene);
            dStage.showAndWait();

        } else if (dialogStage.getResult() == buttonTypeTwo) {
            Them = "";
            S = "";
            for (int i = 0; i <= n; i++) {
                arrayInt[i] = 0;
            }
            taCongdung.setText("");
            taThem.setText("");
            tiepNhanCtrl.themTaThuoc("");
            KeDonThuocData.clear();
            initTable(ThuocData);
        }
    }

    public void addThuocData() throws SQLException {
        String sql = "SELECT * FROM Thuoc ORDER BY Ma_Thuoc ASC";
        con = new ConnectToServer();
        con.sendToServer(sql);
        while (true) {
            Object ob = con.receiveFromServer();
            /*
             * if returned object is not CacheRowSet, inform and end the loop
             * else use CacheRowSet and end the loop
             */
            if (ob.getClass().toString().equals("class java.lang.String")) {
                break;
            } else {
                CachedRowSet crs = (CachedRowSet) ob;
                while (crs.next()) {
                    Thuoc thuoc = new Thuoc();
                    thuoc.setMa(crs.getInt("Ma_Thuoc"));
                    thuoc.setTenThuoc(crs.getString("Ten_Thuoc"));
                    thuoc.setCongDung(crs.getString("Cong_Dung"));
                    thuoc.setGiaThuoc(crs.getFloat("Gia_Thuoc"));
                    thuoc.setDonVi(crs.getString("Don_Vi"));
                    thuoc.setSoLuong(crs.getInt("So_Luong"));
                    ThuocData.add(thuoc);

                }
                break;
            }
        }
        con.sendToServer("done");
    }

    public void initTable(ObservableList<Thuoc> Data) throws SQLException {
        ColThuocMa.setCellValueFactory(new PropertyValueFactory<>("ma"));
        ColThuocTenthuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        ColThuocGiathuoc.setCellValueFactory(new PropertyValueFactory<>("giaThuoc"));
        ColThuocCongdung.setCellValueFactory(new PropertyValueFactory<>("congDung"));
        ColThuocSoluong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        ColThuocDonvi.setCellValueFactory(new PropertyValueFactory<>("donVi"));

        FilteredList<Thuoc> filteredData = new FilteredList<>(Data, p -> true);
        ThuocTable.setItems(filteredData);
        tfLoc.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteredData.setPredicate((Thuoc thuoc) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (cbLoc.getValue()) {
                    case "Mã":
                        if (thuoc.getMa() == Integer.parseInt(newValue)) {
                            return true;
                        }
                        break;
                    case "Tên Thuốc":
                        if (thuoc.getTenThuoc().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Giá":
                        if (thuoc.getGiaThuoc() == Float.parseFloat(newValue)) {
                            return true;
                        }
                    case "Công Dụng":
                        if (thuoc.getCongDung().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                }
                return false;
            });
        });
        ThuocTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showDetails(newValue));
    }

    private void cbSearchInit() {
        cbLoc.getItems().addAll("Mã", "Tên Thuốc", "Giá", "Công Dụng");
        cbLoc.setValue("Mã");
    }

    //override tu Initializable interface
    private void showDetails(Thuoc thuoc) {
        if (thuoc != null) {
            Ma = thuoc.getMa();
            lblTenThuoc.setText(thuoc.getTenThuoc());
            Giaf = thuoc.getGiaThuoc();
            Gia = Float.toString(thuoc.getGiaThuoc());
            taCongdung.setText(thuoc.getCongDung());
            lblGiaThuoc.setText(Gia);
            lblDonVi.setText(thuoc.getDonVi());
            String soluong;
            soluong = Integer.toString(thuoc.getSoLuong());
            lblSoLuong.setText(soluong);
        } else {
            lblTenThuoc.setText("");
            lblGiaThuoc.setText("");
            lblDonVi.setText("");
            lblSoLuong.setText("");
        }
    }

    public ObservableList<KeDonThuoc> getKeDonThuocData() {
        return KeDonThuocData;
    }

    //xoa observable list
    public void deleteMemoryT() {
        Them = "";
        S = "";
        taThem.setText(Them);
        tiepNhanCtrl.themTaThuoc(Them);
        KeDonThuocData.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ControllerMediator.getInstance().setThuocCtrl(this);

            cbSearchInit();
            addThuocData();
            initTable(ThuocData);

        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //override tu PaneInterface interface

    @Override
    public void setScreenParent(BacSiController mainPane) {
        parentPane = mainPane;
    }

}
