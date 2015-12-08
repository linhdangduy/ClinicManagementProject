/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.DuocSi;

import cm.ConnectToDatabase;
import cm.ConnectToServer;
import cm.model.Thuoc;
import java.net.URL;
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
import javax.sql.rowset.CachedRowSet;

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
    
    /*private ConnectToDatabase con;
    private PreparedStatement ps;
    private ResultSet rs;*/
    private ConnectToServer con;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //con = new ConnectToDatabase();
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
        con = new ConnectToServer();
        String sql = "select * from Thuoc";
        ThuocData.clear();
        con.sendToServer(sql);
        while (true){
            Object ob = con.receiveFromServer();
            /*
            * if returned object is not CacheRowSet, inform and end the loop
            * else use CacheRowSet and end the loop
           */
            if (ob.getClass().toString().equals("class java.lang.String")) {
                break;
            }
            else{
                CachedRowSet crs = (CachedRowSet)ob;
                while (crs.next()){
                    Thuoc thuoc = new Thuoc();
                    thuoc.setMa(crs.getInt("Ma_Thuoc"));
                    thuoc.setTenThuoc(crs.getString("Ten_Thuoc"));
                    thuoc.setCongDung(crs.getString("Cong_Dung"));
                    thuoc.setDonVi(crs.getString("Don_Vi"));
                    thuoc.setGiaThuoc(crs.getFloat("Gia_Thuoc"));
                    thuoc.setSoLuong(crs.getInt("So_Luong"));
                    thuoc.setGhiChu(crs.getString("Ghi_Chu_Thuoc"));
                    ThuocData.add(thuoc);
                }
                break;
            }
        }
        con.sendToServer("done");
        /*rs = con.getRS(sql);
        while (rs.next()){
            Thuoc thuoc = new Thuoc();
            thuoc.setMa(rs.getInt("Ma_Thuoc"));
            thuoc.setTenThuoc(rs.getString("Ten_Thuoc"));
            thuoc.setCongDung(rs.getString("Cong_Dung"));
            thuoc.setDonVi(rs.getString("Don_Vi"));
            thuoc.setGiaThuoc(rs.getFloat("Gia_Thuoc"));
            thuoc.setSoLuong(rs.getInt("So_Luong"));
            thuoc.setGhiChu(rs.getString("Ghi_Chu_Thuoc"));
            ThuocData.add(thuoc);
        }
        rs.close();*/
        
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
    private void Them(ActionEvent event) throws SQLException {
        if(thuocSelected !=null){
            Alert alert1 = new Alert(AlertType.CONFIRMATION , "Xác Nhận Thêm Thuốc? " , ButtonType.YES, ButtonType.NO);
            alert1.showAndWait();
            if (alert1.getResult() == ButtonType.YES){
                if (!tfTenThuoc.getText().isEmpty() 
                        && !tfCongDung.getText().isEmpty() 
                        && !cbDonVi.getValue().equals("Đơn Vị") 
                        && !tfGiaThuoc.getText().isEmpty()
                        && !tfSoLuong.getText().isEmpty()
                        && !tfGhiChu.getText().isEmpty()
                        )
                {
                    con = new ConnectToServer();
                    /*String sql = "insert into Thuoc values (NULL, ?,?,?,?,?,?)";
                    ps = con.getPS(sql);
                    ps.setString(1,tfTenThuoc.getText());
                    ps.setFloat(2, Float.parseFloat(tfGiaThuoc.getText()));
                    ps.setString(3,tfCongDung.getText());
                    ps.setInt(4, Integer.parseInt(tfSoLuong.getText()));
                    ps.setString(5, cbDonVi.getValue());
                    ps.setString(6,tfGhiChu.getText());
                    ps.executeUpdate();
                    ps.close();*/
                    String sql = "insert into Thuoc values (NULL, '"
                            + tfTenThuoc.getText() +"','"
                            + tfGiaThuoc.getText()+"','"
                            + tfCongDung.getText() + "','"
                            + tfSoLuong.getText()+"','"
                            + cbDonVi.getValue()+"','"
                            + tfGhiChu.getText()+"')";
                    con.sendToServer(sql);
                    con.sendToServer("done");

                    addThuocData();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Bạn đã nhập thiếu thông tin" , ButtonType.OK);
                    alert.showAndWait();
                }    
            }    
        }
        else{
            Alert alert2 = new Alert(AlertType.ERROR ,"Chưa Chọn Thuốc" , ButtonType.OK);
            alert2.showAndWait();
        }
        
         
        
        
    }

    @FXML
    private void Huy(ActionEvent event) {
        tfTenThuoc.setText(null);
        tfCongDung.setText(null);
        cbDonVi.setValue("Đơn Vị");
        tfGiaThuoc.setText(null);
        tfSoLuong.setText(null);
        tfGhiChu.setText(null);
    }

    @FXML
    private void Sua(ActionEvent event) throws SQLException {
        if(thuocSelected !=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION , "Xác Nhận Thêm Thuốc? " , ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES){
                if (!tfTenThuoc1.getText().isEmpty() 
                        && !tfCongDung1.getText().isEmpty() 
                        && !cbDonVi1.getValue().equals("Đơn Vị") 
                        && !tfGiaThuoc1.getText().isEmpty()
                        && !tfSoLuong1.getText().isEmpty()
                        && !tfGhiChu1.getText().isEmpty()
                        )
                {
                    /*String sql = "update Thuoc set Ten_Thuoc = ? , Cong_Dung = ? , Don_Vi =? , Gia_Thuoc =? , So_Luong =? ,Ghi_Chu_Thuoc =? where Ma_Thuoc =? ";
                    ps = con.getPS(sql);
                    ps.setString(1, tfTenThuoc1.getText());
                    ps.setString(2, tfCongDung1.getText());
                    ps.setString(3,cbDonVi1.getValue());
                    ps.setFloat(4, Float.parseFloat(tfGiaThuoc1.getText()));
                    ps.setInt(5, Integer.parseInt(tfSoLuong1.getText()));
                    ps.setString(6, tfGhiChu1.getText());
                    ps.setInt(7, thuocSelected.getMa());
                    ps.executeUpdate();
                    ps.close();

                    thuocSelected.setTenThuoc(tfTenThuoc1.getText());
                    thuocSelected.setCongDung(tfCongDung1.getText());
                    thuocSelected.setDonVi(cbDonVi1.getValue());
                    thuocSelected.setGiaThuoc(Float.parseFloat(tfGiaThuoc1.getText()));
                    thuocSelected.setSoLuong(Integer.parseInt(tfSoLuong1.getText()));
                    thuocSelected.setGhiChu(tfGhiChu1.getText());
                    ThuocData.set(ThuocData.indexOf(thuocSelected), thuocSelected);*/
                    con =new ConnectToServer();
                    String sql = "update Thuoc set Ten_Thuoc = '"
                            + tfTenThuoc1.getText()+"' , Cong_Dung = '"
                            + tfCongDung1.getText()+"' , Don_Vi ='"
                            + cbDonVi1.getValue()+"' , Gia_Thuoc ='"
                            + tfGiaThuoc1.getText() +"' , So_Luong ='"
                            + tfSoLuong1.getText()+"' ,Ghi_Chu_Thuoc ='"
                            + tfGhiChu1.getText()+"' where Ma_Thuoc ='"
                            + thuocSelected.getMa()+"' ";
                    con.sendToServer(sql);
                    con.sendToServer("done");
                    addThuocData();
                }
                else{
                    Alert alert1 = new Alert(Alert.AlertType.ERROR , "Thiếu thông tin" , ButtonType.OK);
                    alert1.showAndWait();
                }    
            }
        }
        else{
            Alert alert2 = new Alert(AlertType.ERROR ,"Chưa Chọn Thuốc" , ButtonType.OK);
            alert2.showAndWait();
        }
        
            
    }

    @FXML
    private void Xoa(ActionEvent event) throws SQLException {
        if(thuocSelected !=null){
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION , "Xác Nhận Xóa Thuốc?", ButtonType.YES, ButtonType.NO  );
            alert1.showAndWait();
            if(alert1.getResult() == ButtonType.YES){
                if (thuocSelected != null){
                    con = new ConnectToServer();
                    String sql = "delete from Thuoc where Ma_Thuoc =' "+thuocSelected.getMa() + "'";
                    /*ps = con.getPS(sql);
                    ps.setInt(1, thuocSelected.getMa());
                    ps.executeUpdate();
                    ps.close();
                    ThuocData.remove(thuocSelected);*/
                    con.sendToServer(sql);
                    con.sendToServer("done");
                    addThuocData();
                }    
            }
        }
        else{
            Alert alert2 = new Alert(AlertType.ERROR ,"Chưa Chọn Thuốc" , ButtonType.OK);
            alert2.showAndWait();
        }

        
            
    }
    

    
    
}
