/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author linhsan
 */
public class KeDonThuoc {
    private SimpleIntegerProperty maThuoc = new SimpleIntegerProperty();
    private SimpleStringProperty tenThuoc = new SimpleStringProperty();
    private SimpleStringProperty tenDangNhap = new SimpleStringProperty();
    private SimpleIntegerProperty soLuong = new SimpleIntegerProperty();
    private SimpleIntegerProperty soLuongKe = new SimpleIntegerProperty();
    private SimpleFloatProperty chiPhiThuoc = new SimpleFloatProperty();
    private SimpleStringProperty cachDungThuoc = new SimpleStringProperty();
    
    public void setMa(int s) {
        this.maThuoc.set(s);
    }
    public void setTenThuoc(String s) {
        this.tenThuoc.set(s);
    }
    public void setTenDangNhap(String s) {
        this.tenDangNhap.set(s);
    }
    public void setSoLuong(int s) {
        this.soLuong.setValue(s);
    }
    public void setSoLuongKe(int s) {
        this.soLuongKe.setValue(s);
    }
    public void setChiPhiThuoc(float s) {
        this.chiPhiThuoc.set(s);
    }
    public void setCachDungThuoc(String s) {
        this.cachDungThuoc.set(s);
    }
    
    public int getMaThuoc() {
        return maThuoc.get();
    }
    public String getTenThuoc() {
        return tenThuoc.get();
    }
    public String getTenDangNhap() {
        return tenDangNhap.get();
    }
    public int getSoLuong() {
        return soLuong.get();
    }
    public int getSoLuongKe() {
        return soLuongKe.get();
    }
    public float getChiPhiThuoc() {
        return chiPhiThuoc.get();
    }
    public String getCachDungThuoc() {
        return cachDungThuoc.get();
    }
}
