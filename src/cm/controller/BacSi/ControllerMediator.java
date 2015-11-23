/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.controller.BacSi;

/**
 * class for manage controllers
 * @author linhsan
 */

public class ControllerMediator {

   //create an object of SingleObject
   private static final ControllerMediator instance = new ControllerMediator();
   private ThuocController thuocCtrl;
   private DichVuController dichvuCtrl;
   private TiepNhanController tiepnhanCtrl;
   private KeDonThuocController kedonthuocCtrl;
   private DonDichVuController dondichvuCtrl;
   //make the constructor private so that this class cannot be
   //instantiated
   private ControllerMediator(){}

   //Get the only object available
   public static ControllerMediator getInstance(){
      return instance;
   }
   
   //cac ham set controller
   public void setThuocCtrl(ThuocController thuocCtrl) {
       this.thuocCtrl = thuocCtrl;
   }
   
   public void setDichVuCtrl(DichVuController dichvuCtrl) {
       this.dichvuCtrl = dichvuCtrl;
   }
   
   public void setTiepNhanCtrl(TiepNhanController tiepnhanCtrl) {
       this.tiepnhanCtrl = tiepnhanCtrl;
   }
   public void setKeDonThuocCtrl(KeDonThuocController kedonthuocCtrl){
       this.kedonthuocCtrl =kedonthuocCtrl;
   }
   
     public void setDonDichVuCtrl(DonDichVuController dondichvuCtrl) {
       this.dondichvuCtrl = dondichvuCtrl;
   }
   //cac ham nhan controller
   public ThuocController getThuocCtrl() {
       return thuocCtrl;
   }
   
   public DichVuController getDichVuCtrl() {
       return dichvuCtrl;
   }
   
   public TiepNhanController getTiepNhanCtrl() {
       return tiepnhanCtrl;
   }
    public KeDonThuocController getKeDonThuocCtrl() {
       return kedonthuocCtrl;
   }
    public DonDichVuController getDonDichVuCtrl() {
       return dondichvuCtrl;
   }
   public void showMessage(){
      System.out.println("Hello World!");
   }
}
