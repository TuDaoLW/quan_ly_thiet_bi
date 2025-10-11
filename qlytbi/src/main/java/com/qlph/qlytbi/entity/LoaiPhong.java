package com.qlph.qlytbi.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "loai_phong")
@Data
public class LoaiPhong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loai_phong")
    private Integer idLoaiPhong;

    @NotBlank(message = "Tên loại phòng không được để trống")
    @Size(max = 50, message = "Tên loại phòng không được vượt quá 50 ký tự")
    @Column(name = "ten_loai", unique = true)
    private String tenLoai;

    @Column(name = "mo_ta")
    private String moTa;
}