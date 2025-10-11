package com.qlph.qlytbi.repository;

import com.qlph.qlytbi.entity.PhongTB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhongTBRepository extends JpaRepository<PhongTB, Integer> {
    PhongTB findByPhongHocIdPhong(Integer idPhong);
}