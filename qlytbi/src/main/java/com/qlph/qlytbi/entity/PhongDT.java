package com.qlph.qlytbi.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "phong_dt")
@Data
public class PhongDT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong_dt")
    private Integer idPhongDt;

    @OneToOne
    @JoinColumn(name = "id_phong", unique = true, nullable = false)
    private PhongHoc phongHoc;

    @Size(max = 100, message = "Chuyên ngành không được vượt quá 100 ký tự")
    @Column(name = "chuyen_nganh")
    private String chuyenNganh;
}