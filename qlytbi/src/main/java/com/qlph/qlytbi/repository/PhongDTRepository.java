package com.qlph.qlytbi.repository;

import com.qlph.qlytbi.entity.PhongDT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhongDTRepository extends JpaRepository<PhongDT, Integer> {
    PhongDT findByPhongHocIdPhong(Integer idPhong);
}