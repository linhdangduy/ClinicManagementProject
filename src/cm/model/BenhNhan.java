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
public class BenhNhan {
    private SimpleIntegerProperty Ma = new SimpleIntegerProperty();
    private SimpleStringProperty HoTen = new SimpleStringProperty();
    private SimpleStringProperty NgaySinh = new SimpleStringProperty();
    private SimpleStringProperty GioiTinh = new SimpleStringProperty();
    private SimpleStringProperty Phone = new SimpleStringProperty();
    private SimpleStringProperty ThoiGian = new SimpleStringProperty();
    private SimpleStringProperty TrangThai = new SimpleStringProperty();
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
    public String getTrangThai(){
        return TrangThai.get();
    }
    public String getDiaChi(){
        return DiaChi.get();
    }
    public void setMa(int Ma){
        this.Ma.set(Ma);
    }
    public void setHoTen(String HoTen){
        this.HoTen.set(HoTen);
    }
    public void setNgaySinh(String NgaySinh){
        this.NgaySinh.set(NgaySinh);
    }
    public void setGioiTinh(String GioiTinh){
        this.GioiTinh.set(GioiTinh);
    }
    public void setPhone(String Phone){
        this.Phone.set(Phone);
    }
    public void setTrangThai(String TrangThai){
        this.TrangThai.set(TrangThai);
    }
    public void setDiaChi(String DiaChi){
        this.DiaChi.set(DiaChi);
    }
}
