/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

import cm.model.DonDichVu;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mai Hoang Duc
 */
public class DonDichVuController implements Initializable {

    @FXML
    private TableView<DonDichVu> DonDichVuTable;
    @FXML
    private TableColumn ColDonTendichvu;
    @FXML
    private TableColumn ColDonKetqua;
    @FXML
    private Label lblShow;

    private DichVuController DichVuCtrl = ControllerMediator.getInstance().getDichVuCtrl();
    public ObservableList<DonDichVu> Data = FXCollections.observableArrayList(DichVuCtrl.getDonDichVuData());

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ControllerMediator.getInstance().setDonDichVuCtrl(this);
        initable();
        setEditable();
    }

    @FXML
    private void handleBtnOk(ActionEvent event) {
        Stage stage = DichVuCtrl.dStage;
        stage.hide();
    }

    private void initable() {
        ColDonTendichvu.setCellValueFactory(new PropertyValueFactory<>("tenDichVu"));
        ColDonKetqua.setCellValueFactory(new PropertyValueFactory<>("ketQua"));
        DonDichVuTable.setItems(Data);

    }

    private void setEditable() {
        DonDichVuTable.setEditable(true);

        ColDonKetqua.setEditable(true);
        ColDonKetqua.setCellFactory(TextFieldTableCell.forTableColumn());
        ColDonKetqua.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DonDichVu, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DonDichVu, String> t) {
                DonDichVu dichvu = ((DonDichVu) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                dichvu.setKetQua(t.getNewValue());

            }
        });
    }
}
