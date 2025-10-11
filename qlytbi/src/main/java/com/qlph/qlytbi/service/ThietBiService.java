package com.qlph.qlytbi.service;

import com.qlph.qlytbi.entity.PhongHoc;
import com.qlph.qlytbi.entity.ThietBi;
import com.qlph.qlytbi.repository.PhongHocRepository;
import com.qlph.qlytbi.repository.ThietBiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class ThietBiService {

    private static final Logger log = LoggerFactory.getLogger(ThietBiService.class);

    @Autowired
    private ThietBiRepository repo;

    @Autowired
    private PhongHocRepository phongHocRepo;

    public List<ThietBi> getAll() {
        log.info("===> [Service] Fetching all thiết bị...");
        List<ThietBi> list = repo.findAll();
        log.info("===> [Service] Fetched {} thiết bị", list.size());
        return list;
    }

    public ThietBi getById(Integer id) {
        log.info("===> [Service] Fetching thiết bị id={}", id);
        return repo.findById(id).orElse(null);
    }

    public ThietBi save(ThietBi tb) {
        log.info("===> [Service] Saving thiết bị: {}", tb);
        // Làm sạch dữ liệu
        tb.setTenThietBi(Jsoup.clean(tb.getTenThietBi(), Safelist.basic()));
        tb.setMoTa(Jsoup.clean(tb.getMoTa() != null ? tb.getMoTa() : "", Safelist.basic()));
        // Kiểm tra tính duy nhất của maThietBi
        Optional<ThietBi> existing = repo.findByMaThietBi(tb.getMaThietBi());
        if (existing.isPresent() && !existing.get().getIdThietBi().equals(tb.getIdThietBi())) {
            throw new IllegalArgumentException("Mã thiết bị đã tồn tại!");
        }
        if (tb.getPhongHoc() == null || tb.getPhongHoc().getIdPhong() == null || 
            "K00".equals(tb.getPhongHoc().getMaPhong())) {
            PhongHoc kho = phongHocRepo.findByMaPhong("K00");
            if (kho == null) {
                throw new IllegalArgumentException("Phòng kho với mã K00 không tồn tại!");
            }
            tb.setPhongHoc(kho);
            tb.setNgayLapDat(null);
        }
        try {
            return repo.save(tb);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Lỗi lưu dữ liệu: Mã thiết bị đã tồn tại hoặc dữ liệu không hợp lệ!");
        }
    }

    public void delete(Integer id) {
        log.warn("===> [Service] Deleting thiết bị id={}", id);
        try {
            if (!repo.existsById(id)) {
                log.error("===> [Service] Thiết bị id={} không tồn tại", id);
                throw new IllegalArgumentException("Thiết bị không tồn tại");
            }
            repo.deleteById(id);
            log.info("===> [Service] Thiết bị id={} deleted successfully", id);
        } catch (IllegalArgumentException e) {
            log.error("===> [Service] Error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("===> [Service] Unexpected error deleting thiết bị id={}: {}", id, e.getMessage(), e);
            throw new IllegalArgumentException("Lỗi khi xóa thiết bị");
        }
    }

    public List<ThietBi> search(String keyword) {
        log.info("===> [Service] Searching thiết bị by keyword='{}'", keyword);
        List<ThietBi> list = repo.findByTenThietBiContainingIgnoreCase(keyword);
        log.info("===> [Service] Found {} thiết bị matching keyword", list.size());
        return list;
    }

    public List<ThietBi> getByPhongHoc(Integer idPhong) {
        log.info("===> [Service] Fetching thiết bị for phòng id={}", idPhong);
        List<ThietBi> list = repo.findByPhongHocIdPhong(idPhong);
        log.info("===> [Service] Fetched {} thiết bị for phòng id={}", list.size(), idPhong);
        return list;
    }
}