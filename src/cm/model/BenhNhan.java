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
public class BenhNhan extends Nguoi {
    private SimpleStringProperty ThoiGian = new SimpleStringProperty();
    private SimpleStringProperty TrangThai = new SimpleStringProperty();
    
    public String getTrangThai(){
        return TrangThai.get();
    }
    public String getThoiGian(){
        return ThoiGian.get();
    }
    public void setTrangThai(String s){
        this.TrangThai.set(s);
    }
    public void setThoiGian(String s){
        this.ThoiGian.set(s);
    }
}
