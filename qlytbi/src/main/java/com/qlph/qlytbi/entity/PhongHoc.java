package com.qlph.qlytbi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "phong_hoc")
@Data
public class PhongHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong")
    private Integer idPhong;

    @Column(name = "ma_phong")
    private String maPhong;

    @Column(name = "ten_phong")
    private String tenPhong;

    @Column(name = "suc_chua")
    private Integer sucChua;

    @Column(name = "vi_tri")
    private String viTri;

    @Column(name = "id_loai_phong")
    private Integer idLoaiPhong;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "mo_ta")
    private String moTa;
}