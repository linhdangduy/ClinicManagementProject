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
public class Dichvu {
   private SimpleIntegerProperty ma= new SimpleIntegerProperty();
   private SimpleStringProperty tenDichVu= new SimpleStringProperty();
   private SimpleStringProperty chucNang= new SimpleStringProperty();
   private SimpleFloatProperty gia= new SimpleFloatProperty(); 
   
   
   
   public int getMa()
   {
       return ma.get();
   }
    public String getTenDichVu()
    {
        return tenDichVu.get();
    }
    public String getChucNang()
    {
        return chucNang.get();
    }
    public float getGia()
    {
        return gia.get();
    }
    
    public void setMa(int ma)
   {
       this.ma.set(ma);
   }
    public void setTenDichVu(String tenDichVu)
    {
        this.tenDichVu.set(tenDichVu);
    }
    public void setChucNang(String chucNang)
    {
        this.chucNang.set(chucNang);
    }
    public void setGia(float gia)
    {
        this.gia.set(gia);
    }
    
}
