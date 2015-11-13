/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author linhsan
 */
public class NhanVien extends Nguoi {
    private SimpleStringProperty TenDangNhap = new SimpleStringProperty();
    private SimpleStringProperty MatKhau = new SimpleStringProperty();
    private SimpleStringProperty ChuyenNganh = new SimpleStringProperty();
    private SimpleStringProperty BacHoc = new SimpleStringProperty();
    private SimpleStringProperty Phong = new SimpleStringProperty();
    private SimpleStringProperty TrangThai = new SimpleStringProperty();
    
    public String getTendangnhap() {
        return TenDangNhap.getValue();
    }
    public String getMatkhau() {
        return MatKhau.getValue();
    }
    public String getChuyennganh() {
        return ChuyenNganh.getValue();
    }
    public String getBachoc() {
        return BacHoc.getValue();
    }
    public String getPhong() {
        return Phong.getValue();
    }
    public String getTrangthai() {
        return TrangThai.getValue();
    }
    
    public void setTendangnhap(String s) {
        TenDangNhap.setValue(s);
    }
    public void setMatkhau(String s) {
        MatKhau.setValue(s);
    }
    public void setChuyennganh(String s) {
        ChuyenNganh.setValue(s);
    }
    public void setBachoc(String s) {
        BacHoc.setValue(s);
    }
    public void setPhong(String s) {
        Phong.setValue(s);
    }
    public void setTrangthai(String s) {
        TrangThai.setValue(s);
    }
}
