package com.qlph.qlytbi.repository;

import com.qlph.qlytbi.entity.ThietBi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThietBiRepository extends JpaRepository<ThietBi, Integer> {
    List<ThietBi> findByTenThietBiContainingIgnoreCase(String keyword);
    List<ThietBi> findByPhongHocIdPhong(Integer idPhong);
    Optional<ThietBi> findByMaThietBi(String maThietBi);
}