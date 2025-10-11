package com.qlph.qlytbi.repository;

import com.qlph.qlytbi.entity.ThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ThietBiRepository extends JpaRepository<ThietBi, Integer> {
    List<ThietBi> findByTenThietBiContainingIgnoreCase(String keyword);
}
