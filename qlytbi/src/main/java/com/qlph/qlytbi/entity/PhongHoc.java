package com.qlph.qlytbi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PHONG_HOC")
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
}
