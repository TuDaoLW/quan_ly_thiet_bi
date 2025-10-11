package com.qlph.qlytbi.service;

import com.qlph.qlytbi.entity.ThietBi;
import com.qlph.qlytbi.repository.ThietBiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThietBiService {

    private static final Logger log = LoggerFactory.getLogger(ThietBiService.class);

    @Autowired
    private ThietBiRepository repo;

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
        return repo.save(tb);
    }

    public void delete(Integer id) {
        log.warn("===> [Service] Deleting thiết bị id={}", id);
        repo.deleteById(id);
    }

    public List<ThietBi> search(String keyword) {
        log.info("===> [Service] Searching thiết bị by keyword='{}'", keyword);
        List<ThietBi> list = repo.findByTenThietBiContainingIgnoreCase(keyword);
        log.info("===> [Service] Found {} thiết bị matching keyword", list.size());
        return list;
    }
}
