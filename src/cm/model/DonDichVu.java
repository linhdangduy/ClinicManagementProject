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
 * @author Mai Hoang Duc
 */
public class DonDichVu {

    private SimpleStringProperty tenDichVu = new SimpleStringProperty();
    private SimpleIntegerProperty maDichVu = new SimpleIntegerProperty();
    private SimpleStringProperty tenDangNhap = new SimpleStringProperty();
    private SimpleStringProperty ketQua = new SimpleStringProperty();
    private SimpleFloatProperty GiaDichVu = new SimpleFloatProperty();
    
    public void setGiaDichVu(float f){
        GiaDichVu.set(f);
    }
    public void setMaDichVu(int i) {
        maDichVu.set(i);
    }

    public void setTenDangNhap(String s) {
        tenDangNhap.set(s);
    }

    public void setKetQua(String s) {
        ketQua.set(s);
    }

    public void setTenDichVu(String s) {
        tenDichVu.set(s);
    }

    public int getMaDichVu() {
        return maDichVu.get();
    }

    public String getTenDangNhap() {
        return tenDangNhap.get();
    }

    public String getKetQua() {
        return ketQua.get();
    }

    public String getTenDichVu() {
        return tenDichVu.get();
    }
    public float getGiaDichVu() {
        return GiaDichVu.get();
    }
}
