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
 * @author hoangduc
 */
public class Thuoc {
        public SimpleIntegerProperty ma = new SimpleIntegerProperty();
        public SimpleStringProperty tenThuoc = new SimpleStringProperty();
        public SimpleStringProperty congDung = new SimpleStringProperty();
        public SimpleStringProperty donVi = new SimpleStringProperty();
        public SimpleFloatProperty giaThuoc = new SimpleFloatProperty();
        public SimpleStringProperty trangThai = new SimpleStringProperty();
        
  
        public Integer getMa() {
            return ma.get();
        }

        public String getTenThuoc() {
            return tenThuoc.get();
        }

        public Float getGiaThuoc() {
            return giaThuoc.get();
        }

        public String getDonVi() {
            return donVi.get();
        }

        public String getTrangThai() {
            return trangThai.get();
        }
        public String getCongDung()
        {
            return congDung.get();
        }
        public void setMa(int ma)
        {
             this.ma.set(ma);
        }
        
        public void setTenThuoc(String tenThuoc)
        {
            this.tenThuoc.set(tenThuoc);
        }
        
        public void setGiaThuoc(float giaThuoc)
        {
            this.giaThuoc.set(giaThuoc);
        }
        
        public void setDonVi(String donVi)
        {
            this.donVi.set(donVi);
        }
        
        public void setTrangThai(String trangThai)
        {
            this.trangThai.set(trangThai);
        }
        
        public void setCongDung(String congDung)
        {
            this.congDung.set(congDung);
        }
}
