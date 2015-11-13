package cm.model;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vinh
 */
public class Nguoi {
    private SimpleIntegerProperty Ma = new SimpleIntegerProperty();
    private SimpleStringProperty HoTen = new SimpleStringProperty();
    private SimpleStringProperty NgaySinh = new SimpleStringProperty();
    private SimpleStringProperty GioiTinh = new SimpleStringProperty();
    private SimpleStringProperty Phone = new SimpleStringProperty();
    private SimpleStringProperty DiaChi = new SimpleStringProperty();
    
    public int getMa(){
        return Ma.get();
    }
    public String getHoTen(){
        return HoTen.get();
    }
    public String getNgaySinh(){
        return NgaySinh.get();
    }
    public String getGioiTinh(){
        return GioiTinh.get();
    }
    public String getPhone(){
        return Phone.get();
    }
    public String getDiaChi(){
        return DiaChi.get();
    }
    public void setMa(int Ma){
        this.Ma.set(Ma);
    }
    public void setHoTen(String s){
        this.HoTen.set(s);
    }
    public void setNgaySinh(String s){
        this.NgaySinh.set(s);
    }
    public void setGioiTinh(String s){
        this.GioiTinh.set(s);
    }
    public void setPhone(String s){
        this.Phone.set(s);
    }
    
    public void setDiaChi(String s){
        this.DiaChi.set(s);
    }
}
