package com.qlph.qlytbi.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "THIET_BI")
@Data
public class ThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thiet_bi")
    private Integer idThietBi;

    @ManyToOne
    @JoinColumn(name = "id_phong", nullable = false)
    private PhongHoc phongHoc;

    @Column(name = "ten_thiet_bi")
    private String tenThietBi;

    @Column(name = "ma_thiet_bi", unique = true)
    private String maThietBi;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "tinh_trang")
    private String tinhTrang;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_lap_dat")
    private Date ngayLapDat;

    @Column(name = "mo_ta")
    private String moTa;
}
