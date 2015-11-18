SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/* có 8 bảng, thứ tự create:
 * Tai_Khoan, Benh_Nhan, Phien_Kham, Thuoc, Don_Thuoc, Dich_Vu, Don_Dich_Vu, Thanh_Toan
 *
 * run: 
 *
 * cd vào thư mục chứa database.sql => 
 * mysql -u root -p < database.sql
 *
 * để kiểm tra thuộc tính của bảng: desc [tên bảng];
*/

/*
 * FORM OF DATE: YYYY-MM-DD => 2000-12-25 (ngày sinh)
 * FORM OF DATETIME: YYYY-MM-DD HH:MM:SS => 2000-12-25 21:30:00 (thời gian khám)
 * giới tính: enum('Nam', 'Nữ')
*/

/*===================bảng tài khoản===========================*/
/*
 * mỗi người một tài khoản.
 * phân quyền tài khoản lễ tân(lễ tân), bác sĩ(phòng khám), thuốc(thuốc) bởi `Phong`,
 * Phong = 'admin' là admin
 */

DROP DATABASE IF EXISTS clinic;
CREATE DATABASE IF NOT EXISTS clinic;
USE clinic;

DROP TABLE IF EXISTS `Tai_Khoan`;
CREATE TABLE `Tai_Khoan`(
	`Ten_Dang_Nhap` VARCHAR(20) NOT NULL,
	PRIMARY KEY(`Ten_Dang_Nhap`),
	`Mat_Khau` VARCHAR(15) NOT NULL,
	`Ten_Nhan_Vien` CHAR(30) NOT NULL,
	`Ngay_Sinh` DATE,
	`Dia_Chi` VARCHAR(100),
	`Gioi_Tinh` ENUM('Nam', 'Nữ'),
	`SDT` CHAR(12),
	`Chuyen_Nganh` VARCHAR(20),
	`Bac_Hoc`CHAR(14),
	`Phong` VARCHAR(20) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*===============================================================*/




INSERT INTO `Tai_Khoan` VALUES
('hoangduc','12345678','Mai Hoàng ĐỨc','1995-1-1','Hà Nội','Nam','0949887586','Đa Khoa','Thạc Sĩ','phòng khám'),
('vanchien','12345678','Trần Văn Chiến','1996-2-1','Thanh Hóa','Nam','0192741244','Y Tá','Cao Đẳng','lễ tân'),
('thuytrang','12345678','Đặng Thùy Trang','2000-4-3','Nghệ An','Nam','0941247124','Dược','Đại Học','phòng thuốc'),
('thanhtrung','12345678','Nghiêm Thành Trung','1989-2-2','Hà Tĩnh','Nữ','0957255252','Y Tá','Cao Đẳng','lễ tân'),
('trungthanh','12345678','Đới Trung Thành','1968-12-8','Ninh Bình','Nữ','0988888888','Dược','Cao Đẳng','phòng thuốc'),
('vantu','12345678','Trịnh Văn Tú','1999-11-17','Hồ Chí Minh','Nữ','091236174','Ruột','Tiến Sĩ','phòng khám'),
('baduy','12345678','Nguyễn Bá Duy','1996-10-12','Huế','Nam','0912487124','Dạ Dày','Thạc Sĩ','phòng khám'),
('vietnam','12345678','Nguyễn Việt Nam','1990-9-24','Cà Mau','Nam','0924612414','Xương','Thạc sĩ','phòng khám'),
('admin','12345678','Trần Bảo Ngọc','1990-9-24','Cà Mau','Nam','091274124','Gan','Tiến Sĩ','admin');


/*================bảng bệnh nhân===================================*/


DROP TABLE IF EXISTS `Benh_Nhan`;
CREATE TABLE `Benh_Nhan`(
	`Ma_Benh_Nhan` INT UNSIGNED NOT NULL AUTO_INCREMENT,	
	PRIMARY KEY(`Ma_Benh_Nhan`),	
	`Ho_Ten` VARCHAR(30) NOT NULL,
	`Ngay_Sinh` DATE,
	`Dia_chi` VARCHAR(100),
	`Gioi_Tinh` ENUM('Nam', 'Nữ'), 
	`SDT_BN` CHAR(12),
	`Trang_Thai` ENUM('phòng khám','thanh toán','phòng thuốc','kết thúc')
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*=================================================================*/




INSERT INTO `Benh_Nhan` VALUES
(NULL,'Mai Phương Thảo','1995-1-1','Hà Nội','Nữ','094912364','phòng khám'),
(NULL,'Trần Việt Dũng','1996-2-1','Thanh Hóa','Nam','0918716482','phòng khám'),
(NULL,'Đinh Huy Hoàng','2000-4-3','Nghệ An','Nam','0984618237','thanh toán'),
(NULL,'Vũ Phương Thảo','1989-2-2','Hà Tĩnh','Nữ','0914618613','phòng thuốc'),
(NULL,'Mai Phương Lan','1968-12-8','Ninh Bình','Nữ','0947145612','phòng thuốc'),
(NULL,'Trần Văn Chỉnh','1967-11-23','Hải Phòng','Nam','0946526485','phòng khám'),
(NULL,'Lê Hoàng Anh','1999-11-17','Hồ Chí Minh','Nữ','0917846274','phòng khám'),
(NULL,'Trần Văn Việt','1996-10-12','Huế','Nam','0947164526','thanh toán'),
(NULL,'Phan Huy Tuấn','1990-9-24','Cà Mau','Nam','0918465712','phòng thuốc'),
(NULL,'Mã Văn Kỳ','1991-3-2','Đà Lạt','Nam','0918273645','phòng khám'),
(NULL,'Chu Hoàng Kiên','1987-5-4','Đà Nẵng','Nam','0918273641','phòng khám');




/*=================bảng phiên khám===============================================*/




DROP TABLE IF EXISTS `Phien_Kham`;
CREATE TABLE `Phien_Kham`(
	`Ma_Phien_Kham` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(`Ma_Phien_Kham`),
	`Ma_Benh_Nhan` INT UNSIGNED NOT NULL,
	`Thoi_Gian_Kham` DATETIME,
	`Ten_Benh` VARCHAR(40),
	`Trieu_Chung` VARCHAR(100),
	`Huong_Dieu_Tri` VARCHAR(100),
	`Ghi_Chu_BA` VARCHAR(100),
	CONSTRAINT fk_BenhAn_MaBenhNhan 
		FOREIGN KEY(`Ma_Benh_Nhan`) REFERENCES `Benh_Nhan`(`Ma_Benh_Nhan`)
		ON DELETE CASCADE
		ON UPDATE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*=========================================================================*/




INSERT INTO `Phien_Kham` VALUES
(NULL,1,'2015-10-11 8:0:00','Sâu răng','đau','dung thuốc bôi','răng hàm sâu'),
(NULL,2,'2015-10-11 8:10:00','gãy mũi','mũi gãy','cố định mũi thẳng','sau này mũi sẽ không đẹp như trước'),
(NULL,3,'2015-10-11 8:30:00','lệch hàm','lệch quai hàm','chỉnh hình','mức độ nhẹ'),
(NULL,11,'2015-10-11 8:53:00','đau mắt đỏ','đau mắt đỏ','bôi thuốc','mức độ nhẹ'),
(NULL,4,'2015-10-11 9:10:01','Tim đạp nhanh','rối loạn','trấn tĩnh','bị stress'),
(NULL,5,'2015-10-11 10:11:00','đau ruột thừa','đau ruốt thừa','đã cắt bỏ ruột thừa','ruột thừa đau'),
(NULL,6,'2015-10-11 11:12:00','loét dạ dày','đau dạ dày','nhận thuốc về điều trị','đau dạ dày mãn tính'),
(NULL,7,'2015-10-11 12:12:00','gãy xương','xương gãy ống chân','nỗi xương và gia cố phần gãy','xương gãy do đá bóng'),
(NULL,8,'2015-10-11 14:00:00','đau dạ dày','đau dạ dày','nội soi và đưa thuốc về ','đau dạ dày cấp tính'),
(NULL,9,'2015-10-11 14:30:01','trật quai hàm','quai hàm lệch','chỉnh lại quai hàm','trật quai hàm do cười lớn'),
(NULL,10,'2015-10-11 15:00:00','chỉnh hình','bính thường','chỉnh lại khuôn mặt','tránh tác động mạnh đến mặt');



/*===================bảng thuốc=====================================*/

DROP TABLE IF EXISTS `Thuoc`;
CREATE TABLE `Thuoc`(
	`Ma_Thuoc` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(`Ma_Thuoc`),
	`Ten_Thuoc` VARCHAR(30) NOT NULL,
	`Gia_Thuoc` FLOAT UNSIGNED,
	`Cong_Dung` VARCHAR(100),
	`So_Luong` INT UNSIGNED,
	`Don_Vi` CHAR(8),
	`Ghi_Chu_Thuoc` VARCHAR(100)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*====================================================================*/





INSERT INTO `Thuoc` VALUES
(NULL,'Mepiloin','190','giảm đau,ê buốt răng',100,'vỉ','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Canxelo','200','giảm đau',100,'hộp','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Faloicoip','125','giảm đau',200,'lọ','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Gaplo','200','chữa đau mắt đỏ',50,'vỉ','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Mepixilin','300','giảm căng thẳng',200,'hộp','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Ripixilin','600','diệt khuẩn hipilit',300,'vỉ','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Xanxilin','70','giảm đau',90,'hộp','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Xaxirin','50','chống đau dạ dày',80,'vỉ','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Rincaxilin','100','tăng canxi khôi phục tế bào xương nhanh chống',60,'lọ','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Mepixilic','5000','trị trĩ',70,'vỉ','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Emaxilin','450','tăng sức đề kháng',40,'lọ','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Metarin','340','diệt khuẩn kili và giảm đau',20,'hộp','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Mepolin','1200','giảm đau',30,'hộp','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú'),
(NULL,'Zalin','1000','giảm đau',10,'lọ','chống chỉ định cho người dị ứng với thành phần của thuốc và người cho con bú');




/*==================bảng đơn thuốc=================================*/



DROP TABLE IF EXISTS `Don_Thuoc`;
CREATE TABLE `Don_Thuoc` (
	`Ma_Phien_Kham` INT UNSIGNED NOT NULL,
	`Ma_Thuoc` INT UNSIGNED NOT NULL,
	PRIMARY KEY(`Ma_Phien_Kham`,`Ma_Thuoc`),
	`Ten_Dang_Nhap` VARCHAR(20),
	`So_Luong` INT UNSIGNED,
	`Chi_Phi_Thuoc` FLOAT UNSIGNED,
	`Ghi_Chu_Thuoc` VARCHAR(100),
	CONSTRAINT fk_DonThuoc_MaPhienKham 
		FOREIGN KEY(`Ma_Phien_Kham`) REFERENCES `Phien_Kham`(`Ma_Phien_Kham`) 
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT fk_DonThuoc_MaThuoc 
		FOREIGN KEY(`Ma_Thuoc`) REFERENCES `Thuoc`(`Ma_Thuoc`) 
		ON DELETE NO ACTION
		ON UPDATE CASCADE,
	CONSTRAINT fk_DonThuoc_TenDangNhap 
		FOREIGN KEY(`Ten_Dang_Nhap`) REFERENCES `Tai_Khoan`(`Ten_Dang_Nhap`) 
		ON DELETE NO ACTION
)ENGINE=InnoDB DEFAULT CHARSET=utf8;





/*=====================================================================*/




INSERT INTO `Don_Thuoc`(`Ma_Phien_Kham`,`Ma_Thuoc`) VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(5,5),
(6,6),
(7,7),
(8,7),
(9,10),
(10,2),
(11,9);





/*===============bảng dịch vụ=====================================*/



DROP TABLE IF EXISTS `Dich_Vu`;
CREATE TABLE `Dich_Vu` (
	`Ma_Dich_Vu` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(`Ma_Dich_Vu`),
	`Ten_Dich_Vu` VARCHAR(50) NOT NULL,
	`Gia_Dich_Vu` FLOAT UNSIGNED,
	`Chuc_Nang` VARCHAR(100)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*==================================================================*/

INSERT INTO `Dich_Vu` VALUES
(NULL,'Siêu âm 3 chiều',300000,'khám thai'),
(NULL,'Chụp X quang',100000,'chẩn đoán tình trạng ban đầu'),
(NULL,'Xét nghiệm máu',400000,'phân tích mẫu máu'),
(NULL,'Xét nghiệm nước tiểu',400000,'phân tích mẫu nước tiểu');


/*===================bảng đơn dịch vụ================================*/
DROP TABLE IF EXISTS `Don_Dich_Vu`;
CREATE TABLE `Don_Dich_Vu`(
	`Ma_Phien_Kham` INT UNSIGNED NOT NULL,
	`Ma_Dich_Vu` INT UNSIGNED NOT NULL,
	PRIMARY KEY(`Ma_Phien_Kham`,`Ma_Dich_Vu`),
	`Ten_Dang_Nhap` VARCHAR(20),
	`Ket_Qua` VARCHAR(100),
	CONSTRAINT fk_DonDichVu_MaPhienKham 
		FOREIGN KEY(`Ma_Phien_Kham`) REFERENCES `Phien_Kham`(`Ma_Phien_Kham`) 
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT fk_DonDichVu_MaDichVu 
		FOREIGN KEY(`Ma_Dich_Vu`) REFERENCES `Dich_Vu`(`Ma_Dich_Vu`) 
		ON DELETE NO ACTION
		ON UPDATE CASCADE,
	CONSTRAINT fk_DonDichVu_TenDangNhap
		FOREIGN KEY(`Ten_Dang_Nhap`) REFERENCES `Tai_Khoan`(`Ten_Dang_Nhap`) 
		ON DELETE NO ACTION
		ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*==================================================================*/






/*====================bảng thanh toán===============================*/

DROP TABLE IF EXISTS `Thanh_Toan`;
CREATE TABLE `Thanh_Toan`(
	`Ma_Phien_Kham` INT UNSIGNED NOT NULL,
	PRIMARY KEY(`Ma_Phien_Kham`),
	`Ten_Dang_Nhap` VARCHAR(20),
	`Chi_Phi_Thuoc` FLOAT UNSIGNED,
	`Chi_Phi_Dich_Vu` FLOAT UNSIGNED,
	`Chi_Phi` FLOAT UNSIGNED,
	CONSTRAINT fk_ThanhToan_MaPhienKham 
		FOREIGN KEY(`Ma_Phien_Kham`) REFERENCES `Phien_Kham`(`Ma_Phien_Kham`) 
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT fk_ThanhToan_TenDanhNhap 
		FOREIGN KEY(`Ten_Dang_Nhap`) REFERENCES `Tai_Khoan`(`Ten_Dang_Nhap`) 
		ON DELETE NO ACTION
		ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*====================================================================*/


