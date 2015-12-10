/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

import cm.ConnectToServer;
import cm.controller.DangNhap.DangNhapController;
import cm.model.BenhNhan;
import cm.model.DonDichVu;
import cm.model.KeDonThuoc;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javax.sql.rowset.CachedRowSet;
/**
 *
 * @author linhsan
 */
public class TiepNhanController implements Initializable, PaneInterface {
    
    private ConnectToServer con;
    
    private BacSiController parentPane;
    
    @FXML
    private Button btnChiTiet;
    @FXML
    private VBox boxDanhSachBN;
    @FXML
    private TabPane tabDieuTri;
    @FXML
    private Label lblHoTen;
    @FXML
    private Label lblNgaySinh;
    @FXML
    private Label lblGioiTinh;
    @FXML
    private Label lblDiaChi;
    @FXML
    private Label lblSoDienThoai;
    @FXML
    private Label lblCanhBao;
    @FXML
    private ComboBox<String> cbSearch, cbSearchTime;
    @FXML
    private TextField tfLoc;
    @FXML
    private TextField tfTrieuChung, tfTenBenh;
    @FXML
    private TextArea taGhiChu, taHuongDieuTri, taThemThuoc, taThemDichVu;
    @FXML
    private TextArea taHisTrieuChung, taHisTenBenh, taHisHuongDieuTri, taHisGhiChu,
            taHisDonThuoc, taHisDonDichVu;
    @FXML
    private ListView<String> lvPhienKham;
    @FXML
    private TableView<BenhNhan> tblBenhNhan;
    @FXML
    private TableColumn colMa, colHoten, colNgaysinh, colGioitinh, 
            colSdt, colThoigiankham, colTrangthai;
    private ObservableList<BenhNhan> benhnhanData = FXCollections.observableArrayList();
    private ThuocController thuocCtrl = ControllerMediator.getInstance().getThuocCtrl();
    private ObservableList<KeDonThuoc> keDonThuocData;
    private DichVuController dichVuCtrl = ControllerMediator.getInstance().getDichVuCtrl();
    private ObservableList<DonDichVu> donDichVuData;
    
    private ObservableList<String> time = FXCollections.observableArrayList();
    private HashMap<String, Integer> inforPK = new HashMap<>();
    private BenhNhan benhnhan = null;
    private Integer maPKShow;
    //override tu Initializable interface
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createConnection();
        initTable();
        cbSearchInit();
        lvPhienKham.setItems(time);
    
        lvPhienKham.getSelectionModel().selectedItemProperty().
               addListener((observable, oldValue, newValue) -> showDetailPK(newValue));
        ControllerMediator.getInstance().setTiepNhanCtrl(this);
    }
    
    private void cbSearchInit() {
        cbSearchTime.getItems().addAll("Hôm Nay" , "Tất Cả");
        cbSearchTime.setValue("Tất Cả");
        cbSearch.getItems().addAll("Mã", "Họ tên", "Số điện thoại");
        cbSearch.setValue("Họ Tên");
    }
    private void initTable() {
        colMa.setCellValueFactory(new PropertyValueFactory<>("Ma"));
        colHoten.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        colNgaysinh.setCellValueFactory(new PropertyValueFactory<>("NgaySinh"));
        colGioitinh.setCellValueFactory(new PropertyValueFactory<>("GioiTinh"));
        colSdt.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        colThoigiankham.setCellValueFactory(new PropertyValueFactory<>("ThoiGian"));
        colTrangthai.setCellValueFactory(new PropertyValueFactory<>("TrangThai"));
        
        FilteredList<BenhNhan> filteredData1 = new FilteredList<>(benhnhanData, p -> true);
        
       cbSearchTime.valueProperty().addListener((observable, oldValue , newValue) -> {
           filteredData1.setPredicate(benhnhan -> {
               switch(newValue){
                   case "Hôm Nay":
                       if (benhnhan.getThoiGian().contains(LocalDate.now().toString()))
                           return true;
                       break;
                   case "Tất Cả":
                       return true;
               }
               return false;
           });
       });
        
        FilteredList<BenhNhan> filteredData2 = new FilteredList<>(benhnhanData, p -> true);
        tblBenhNhan.setItems(filteredData2);
        tfLoc.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData2.setPredicate(bn -> {
                // If filter text is empty, display all
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (cbSearch.getValue()) {
                    case "Mã":
                        if (bn.getMa() == Integer.parseInt(newValue)) {
                            return true; 
                        }
                    break;
                    case "Họ tên":
                        if (bn.getHoTen().toLowerCase().contains(lowerCaseFilter)) {
                            return true; 
                        }
                    break;
                    case "Số điện thoại":
                        if (bn.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                            return true; 
                        }
                    break;
                }
                return false;
            });
        });
        tblBenhNhan.getSelectionModel().selectedItemProperty().addListener(
                observable -> showDetails(tblBenhNhan.getSelectionModel().getSelectedItem()));
    }
    
    private void showDetails(BenhNhan benhnhan) {
        if(benhnhan != null){
            //Show thong tin o tab thong tin
            lblHoTen.setText(benhnhan.getHoTen());
            lblNgaySinh.setText(benhnhan.getNgaySinh());
            lblGioiTinh.setText(benhnhan.getGioiTinh());
            lblSoDienThoai.setText(benhnhan.getPhone());
            lblDiaChi.setText(benhnhan.getDiaChi());
        }
        else{
            lblHoTen.setText("");
            lblNgaySinh.setText("");
            lblGioiTinh.setText("");
            lblSoDienThoai.setText("");
            lblDiaChi.setText("");
        }
    }
    
    
    
    @FXML
    private void handleBtnChiTiet(ActionEvent event) {
        if (tblBenhNhan.getSelectionModel().getSelectedItem() != null) {
            if (!tabDieuTri.isVisible()) {
                BenhNhan bn = tblBenhNhan.getSelectionModel().getSelectedItem();
                if (benhnhan != bn) {
                    clearAll();
                    clearAllHis();
                }               
                benhnhan = bn;
                tabDieuTri.setVisible(true);
                boxDanhSachBN.setVisible(false);
                lblCanhBao.setText("");
                showListView(benhnhan);
            }
        }
        else {
            lblCanhBao.setText("Chưa chọn bệnh nhân nào!");
        }
    }
    //luu phien kham vao ho so benh nhan
    @FXML
    private void handleBtnLuu(ActionEvent event) {
    //  encounter thread error, don't know why.
    //  keDonThuocData = FXCollections.observableArrayList(thuocCtrl.getKeDonThuocData());
    //  donDichVuData = FXCollections.observableArrayList(dichVuCtrl.getDonDichVuData());
        //take donthuoc, dondichvu
        keDonThuocData = ControllerMediator.getInstance().getThuocCtrl().getKeDonThuocData();
        donDichVuData = ControllerMediator.getInstance().getDichVuCtrl().getDonDichVuData();
        BenhNhan benhNhanSelected = tblBenhNhan.getSelectionModel().getSelectedItem();       
        String name = benhNhanSelected.getHoTen();
      
        String tenBenh = tfTenBenh.getText();
        String trieuChung = tfTrieuChung.getText();
        String huongDieuTri = taHuongDieuTri.getText();
        String ghiChu = taGhiChu.getText();
        StringBuilder warning = new StringBuilder();
        warning.append("");
        Alert dialogStage = new Alert(Alert.AlertType.CONFIRMATION);
        dialogStage.setTitle("Lưu phiên khám");
        dialogStage.setHeaderText("Bệnh nhân "+name);
        dialogStage.setResizable(true);
        dialogStage.getDialogPane().setPrefSize(400, 200);
        
        if (tenBenh.isEmpty())
            warning.append("Tên bệnh, ");
        if (trieuChung.isEmpty())
            warning.append("Triệu chứng, ");
        if (huongDieuTri.isEmpty())
            warning.append("Hướng điều trị, ");
        if (ghiChu.isEmpty())
            warning.append("Ghi chú, ");
        if (keDonThuocData.isEmpty())
            warning.append("Đơn thuốc, ");
        if (donDichVuData.isEmpty())
            warning.append("Dịch vụ, ");
        if (warning.length() == 0) {
            dialogStage.setContentText("Bệnh nhân sẽ được chuyển sang phòng thuốc. Bạn có lưu phiên khám này không?"); 
        }
        else {
            warning.deleteCharAt(warning.lastIndexOf(", "));
            String warn = warning.toString();
            dialogStage.setContentText(warn+"còn trống.\n"+ "Bạn vẫn muốn lưu?");
        }
        ButtonType btnLuu = new ButtonType("Lưu");
        ButtonType btnTypeCancel = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogStage.getButtonTypes().setAll(btnLuu, btnTypeCancel);
        Optional<ButtonType> result = dialogStage.showAndWait();
        if (result.get() == btnLuu) {
            try {                
                con = new ConnectToServer();
                //get Ma_Phien_Kham for patient
                String phienKhamHientai = "SELECT Ma_Phien_Kham FROM Phien_Kham "
                        + "WHERE Ma_Benh_Nhan = '"+benhNhanSelected.getMa()
                        + "' ORDER BY Thoi_Gian_Kham DESC LIMIT 1";
                con.sendToServer(phienKhamHientai);
                int maPK = 0;
                CachedRowSet cacheResult = (CachedRowSet)con.receiveFromServer();                       
                cacheResult.next();
                maPK = cacheResult.getInt(1);
                //update Phien_Kham
                String capNhatPK = 
                        "UPDATE Phien_Kham SET Ten_Benh = '"+tenBenh
                            + "', Trieu_Chung = '" + trieuChung
                            + "', Huong_Dieu_Tri = '"+ huongDieuTri
                            + "', Ghi_Chu_BA = '" + ghiChu
                                + "' WHERE Ma_Benh_Nhan = '" + benhNhanSelected.getMa()
                                + "' AND Ma_Phien_Kham = '"+maPK+"'";
                con.sendToServer(capNhatPK);
                //Insert Don_Thuoc
                if (!keDonThuocData.isEmpty()) {
                    StringBuilder capNhatDonThuoc = new StringBuilder(
                            "INSERT INTO Don_Thuoc(Ma_Phien_Kham, Ma_Thuoc, "
                            + "So_Luong_Ke, Chi_Phi_Thuoc, Cach_Dung_Thuoc) VALUES "
                            + "('"+maPK+"','");
                    StringBuilder queryBuild = new StringBuilder();
                    for (KeDonThuoc kdt : keDonThuocData) {
                        System.out.println(maPK+" 1 "+kdt.getMaThuoc()+" 2 "+kdt.getSoLuong()+" 3 "+
                                kdt.getChiPhiThuoc()+" 4 "+kdt.getCachDungThuoc());
                        queryBuild.delete(0, queryBuild.length());
                        queryBuild.append(kdt.getMaThuoc())
                                .append("','").append(kdt.getSoLuong()).append("','")
                                .append(kdt.getChiPhiThuoc()).append("','")
                                .append(kdt.getCachDungThuoc()).append("')");
                        con.sendToServer(capNhatDonThuoc.append(queryBuild).toString());
                    }
                }
                //Insert Don_Dich_Vu
                if (!donDichVuData.isEmpty()) {
                    StringBuilder capNhatDichVu = new StringBuilder(
                            "INSERT INTO Don_Dich_Vu VALUES ('"+maPK+"','");
                    StringBuilder queryBuild = new StringBuilder();
                    for (DonDichVu ddv: donDichVuData) {
                        System.out.println(maPK+" 1 "+ddv.getMaDichVu()+" 2 "+ddv.getTenDangNhap()+" 3 "
                        +ddv.getKetQua());
                        queryBuild.delete(0, queryBuild.length());
                        queryBuild.append(ddv.getMaDichVu()).append("','")
                                .append(ddv.getTenDangNhap()).append("','")
                                .append(ddv.getKetQua()).append("')");
                        con.sendToServer(capNhatDichVu.append(queryBuild).toString());
                    }
                }
                String capNhatTrangThai = "UPDATE Benh_Nhan SET Trang_Thai_BN = 'phòng thuốc' "
                        + "WHERE Ma_Benh_Nhan = '"+benhNhanSelected.getMa()+"'";
                System.out.println(capNhatTrangThai);
                con.sendToServer(capNhatTrangThai);
                con.sendToServer("done");
                benhnhanData.remove(benhNhanSelected);
                clearAll();
                clearAllHis();
                tabDieuTri.setVisible(false);
                boxDanhSachBN.setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }   
    }
    
    public void clearAll() {
        tfTenBenh.setText("");
        tfTrieuChung.setText("");
        taHuongDieuTri.setText("");
        taGhiChu.setText("");
        taThemThuoc.setText("");
        taThemDichVu.setText("");
        ControllerMediator.getInstance().getThuocCtrl().deleteMemoryT();
        ControllerMediator.getInstance().getDichVuCtrl().deleteMemoryDV();        
    }
    
    public void clearAllHis() {
        taHisTenBenh.setText("");
        taHisTrieuChung.setText("");
        taHisDonThuoc.setText("");
        taHisDonDichVu.setText("");
        taHisGhiChu.setText("");
        taHisHuongDieuTri.setText("");
    }
    
    @FXML
    private void handleBtnTroLai(ActionEvent event) {
        tabDieuTri.setVisible(false);
        boxDanhSachBN.setVisible(true);
    }
    
    @FXML
    private void handleBtnKeDon(ActionEvent event) {
        parentPane.setPane("thuoc");
        ControllerMediator.getInstance().getThuocCtrl().setPaneThemThuoc(true);
    }    
    
    @FXML
    private void handleBtnSuDung(ActionEvent event) {
        parentPane.setPane("dichvu");
        ControllerMediator.getInstance().getDichVuCtrl().setPaneThemDichVu(true);
    }
    
    public void themTaThuoc(String s) {
        taThemThuoc.setText(s);
    }
    
    public void themTaDichVu(String s) {
        taThemDichVu.setText(s);
    }
    //show all time of Phien_Kham in history tab
    private void showListView(BenhNhan benhnhan) {
        try {
            inforPK.clear();
            String query = "SELECT Ma_Phien_Kham, Thoi_Gian_Kham FROM Phien_Kham "
                    + "WHERE Ma_Benh_Nhan = '"+benhnhan.getMa()+"' ORDER BY Thoi_Gian_Kham DESC";
            con = new ConnectToServer();
            con.sendToServer(query);
            Object ob = con.receiveFromServer();
            if (ob.getClass().toString().equals("class java.lang.String")) {
                
            }
            else {
                CachedRowSet cacheResult = (CachedRowSet)ob;
                while (cacheResult.next()) {
                    inforPK.put(cacheResult.getString("Thoi_Gian_Kham"), cacheResult.getInt("Ma_Phien_Kham"));
                }
            }
            con.sendToServer("done");
            time.clear();
            time.addAll(inforPK.keySet());
        //    time = FXCollections.observableArrayList(inforPK.keySet());
        //    lvPhienKham.setItems(time);
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //show all information about Phien_Kham in history tab
    private void showDetailPK(String s) {
        try {
            String query;
            maPKShow = inforPK.get(s);
            query = "SELECT Ten_Benh, Trieu_Chung, Huong_Dieu_Tri, Ghi_Chu_BA FROM Phien_Kham "
                    + "WHERE Ma_Phien_Kham = '" + maPKShow +"'";
            con = new ConnectToServer();
            
            con.sendToServer(query);
            Object ob = con.receiveFromServer();
            if (ob.getClass().toString().equals("class java.lang.String")) {
                
            }
            else {
                CachedRowSet cacheResult = (CachedRowSet)ob;
                cacheResult.next();
                taHisTrieuChung.setText(cacheResult.getString("Trieu_Chung"));
                taHisTenBenh.setText(cacheResult.getString("Ten_Benh"));
                taHisHuongDieuTri.setText(cacheResult.getString("Huong_Dieu_Tri"));
                taHisGhiChu.setText(cacheResult.getString("Ghi_Chu_BA"));
            }
            con.sendToServer("done");
            con = new ConnectToServer();
            query = "SELECT Ten_Thuoc, So_Luong_Ke, Cach_Dung_Thuoc FROM Thuoc NATURAL JOIN "
                    + "Don_Thuoc WHERE Ma_Phien_Kham = '"+maPKShow+"'";
            
            con.sendToServer(query);
            Object ob1 = con.receiveFromServer();
                
            if (ob1.getClass().toString().equals("class java.lang.String")) {
                
            }
            else {
                CachedRowSet cacheResult = (CachedRowSet)ob1;
                StringBuilder textBuilder = new StringBuilder();
                while (cacheResult.next()) {
                    textBuilder.append(cacheResult.getString("Ten_Thuoc")).append("- ")
                            .append(cacheResult.getInt("So_Luong_Ke")).append("- ")
                            .append(cacheResult.getString("Cach_Dung_Thuoc")).append("\n");
                }
                System.out.println(textBuilder.toString());
                taHisDonThuoc.setText(textBuilder.toString());
            }
            con.sendToServer("done");
            con = new ConnectToServer();
            query = "SELECT Ten_Dich_Vu, Ket_Qua FROM Dich_Vu NATURAL JOIN Don_Dich_Vu WHERE "
                    + "Ma_Phien_Kham = '"+maPKShow+"'";
            con.sendToServer(query);
            Object ob2 = con.receiveFromServer();
            if (ob2.getClass().toString().equals("class java.lang.String")) {
                
            }
            else {
                CachedRowSet cacheResult = (CachedRowSet)ob2;
                StringBuilder textBuilder = new StringBuilder();
                while (cacheResult.next()) {                
                    textBuilder.append(cacheResult.getString("Ten_Dich_Vu")).append("- ")
                            .append(cacheResult.getString("Ket_Qua")).append("\n");
                }
                taHisDonDichVu.setText(textBuilder.toString());
            }
            con.sendToServer("done");
        } catch (SQLException ex) {
            Logger.getLogger(TiepNhanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void createConnection() {
        try {           
            con = new ConnectToServer();
            String sql = "SELECT * FROM Benh_Nhan NATURAL JOIN (SELECT * FROM Phien_Kham ORDER BY Thoi_Gian_Kham DESC) AS pk "
                        + "GROUP BY Ma_Benh_Nhan "
                        + "ORDER BY Thoi_Gian_Kham asc;";
            con.sendToServer(sql);
            CachedRowSet cacheResult = (CachedRowSet)con.receiveFromServer();
            while (cacheResult.next()) {
                if (cacheResult.getString("Trang_Thai_BN").equals("phòng khám")) {
                    BenhNhan bn = new BenhNhan();
                        bn.setMa(cacheResult.getInt("Ma_Benh_Nhan"));
                        bn.setHoTen(cacheResult.getString("Ho_Ten_BN"));
                        bn.setNgaySinh(cacheResult.getString("Ngay_Sinh_BN"));
                        bn.setGioiTinh(cacheResult.getString("Gioi_Tinh_BN"));
                        bn.setPhone(cacheResult.getString("SDT_BN"));
                        bn.setThoiGian(cacheResult.getString("Thoi_Gian_Kham"));
                        bn.setTrangThai(cacheResult.getString("Trang_Thai_BN"));
                        bn.setDiaChi(cacheResult.getString("Dia_chi_BN"));
                        benhnhanData.add(bn);
                    }
                }
            con.sendToServer("done");
            
        } catch (SQLException ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //override tu PaneInterface interface
    @Override
    public void setScreenParent(BacSiController mainPane) {
        parentPane = mainPane;
    }
}
