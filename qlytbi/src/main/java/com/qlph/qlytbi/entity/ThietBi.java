package com.qlph.qlytbi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.*;

import java.util.Date;

@Entity
@Table(name = "thiet_bi")
@Data
public class ThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thiet_bi")
    private Integer idThietBi;

    @ManyToOne
    @JoinColumn(name = "id_phong", nullable = false)
    private PhongHoc phongHoc;

    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(min = 3, max = 100, message = "Tên thiết bị phải từ 3 đến 100 ký tự")
    @Column(name = "ten_thiet_bi")
    private String tenThietBi;

    @NotBlank(message = "Mã thiết bị không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "Mã thiết bị chỉ được chứa chữ cái, số hoặc dấu gạch ngang")
    @Column(name = "ma_thiet_bi", unique = true)
    private String maThietBi;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    @Column(name = "so_luong")
    private Integer soLuong;

    @NotNull(message = "Tình trạng không được để trống")
    @Pattern(regexp = "^(Tốt|Cần bảo trì|Hỏng)$", message = "Tình trạng phải là Tốt, Cần bảo trì hoặc Hỏng")
    @Column(name = "tinh_trang")
    private String tinhTrang;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Ngày lắp đặt không được là ngày trong tương lai")
    @Column(name = "ngay_lap_dat")
    private Date ngayLapDat;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    @Column(name = "mo_ta")
    private String moTa;
}