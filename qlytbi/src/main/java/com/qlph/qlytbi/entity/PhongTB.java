package com.qlph.qlytbi.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "phong_tb")
@Data
public class PhongTB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong_tb")
    private Integer idPhongTb;

    @OneToOne
    @JoinColumn(name = "id_phong", unique = true, nullable = false)
    private PhongHoc phongHoc;

    @Min(value = 0, message = "Số sinh viên tối đa phải lớn hơn hoặc bằng 0")
    @Column(name = "so_sinh_vien_toi_da")
    private Integer soSinhVienToiDa;

    @Column(name = "loai_thiet_bi")
    private String loaiThietBi;
}