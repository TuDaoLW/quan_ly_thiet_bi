package com.qlph.qlytbi.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.Date;

@Entity
@Table(name = "phong_hoc")
@Data
public class PhongHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong")
    private Integer idPhong;

    @NotBlank(message = "Mã phòng không được để trống")
    @Size(max = 20, message = "Mã phòng không được vượt quá 20 ký tự")
    @Column(name = "ma_phong", unique = true)
    private String maPhong;

    @NotBlank(message = "Tên phòng không được để trống")
    @Size(max = 100, message = "Tên phòng không được vượt quá 100 ký tự")
    @Column(name = "ten_phong")
    private String tenPhong;

    @Min(value = 0, message = "Sức chứa phải lớn hơn hoặc bằng 0")
    @Column(name = "suc_chua")
    private Integer sucChua;

    @Size(max = 200, message = "Vị trí không được vượt quá 200 ký tự")
    @Column(name = "vi_tri")
    private String viTri;

    @ManyToOne
    @JoinColumn(name = "id_loai_phong", nullable = false)
    private LoaiPhong loaiPhong;

    @NotNull(message = "Trạng thái không được để trống")
    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "mo_ta")
    private String moTa;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_tao")
    private Date ngayTao;
}