package com.qlph.qlytbi.controller;

import com.qlph.qlytbi.entity.PhongHoc;
import com.qlph.qlytbi.entity.ThietBi;
import com.qlph.qlytbi.repository.PhongHocRepository;
import com.qlph.qlytbi.service.ThietBiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/thietbi")
public class ThietBiController {

    private static final Logger log = LoggerFactory.getLogger(ThietBiController.class);

    @Autowired
    private ThietBiService service;

    @Autowired
    private PhongHocRepository phongRepo;

    @ModelAttribute("phongs")
    public List<PhongHoc> getAllPhongs() {
        return phongRepo.findAll();
    }

    @GetMapping
    public String list(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("===> [Controller] /thietbi called, keyword={}", keyword);
        List<ThietBi> list = (keyword == null || keyword.isEmpty())
                ? service.getAll()
                : service.search(keyword);
        log.info("===> [Controller] Retrieved {} thiết bị từ DB", list.size());
        model.addAttribute("thietbis", list);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        log.info("===> [Controller] /thietbi/add called");
        model.addAttribute("thietbi", new ThietBi());
        model.addAttribute("phongs", phongRepo.findAll());
        return "form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute ThietBi tb, BindingResult result, Model model) {
        log.info("===> [Controller] Saving thiết bị: {}", tb);
        if (result.hasErrors()) {
            log.warn("===> [Controller] Validation errors: {}", result.getAllErrors());
            model.addAttribute("phongs", phongRepo.findAll());
            return "form";
        }
        try {
            service.save(tb);
            log.info("===> [Controller] Thiết bị saved successfully, redirecting to /thietbi");
            return "redirect:/thietbi?success=Da luu thiet bi thanh cong";
        } catch (IllegalArgumentException e) {
            log.error("===> [Controller] Error: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("phongs", phongRepo.findAll());
            return "form";
        } catch (Exception e) {
            log.error("===> [Controller] Unexpected error saving thiết bị: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi không mong muốn khi lưu thiết bị");
            model.addAttribute("phongs", phongRepo.findAll());
            return "form";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        log.info("===> [Controller] Editing thiết bị id={}", id);
        ThietBi thietBi = service.getById(id);
        if (thietBi == null) {
            log.error("===> [Controller] Thiết bị id={} không tồn tại", id);
            return "redirect:/thietbi?error=Thiet bi khong ton tai";
        }
        model.addAttribute("thietbi", thietBi);
        model.addAttribute("phongs", phongRepo.findAll());
        return "form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        log.warn("===> [Controller] Deleting thiết bị id={}", id);
        try {
            ThietBi thietBi = service.getById(id);
            if (thietBi == null) {
                log.error("===> [Controller] Thiết bị id={} không tồn tại", id);
                return "redirect:/thietbi?error=Thiet bi khong ton tai";
            }
            service.delete(id);
            log.info("===> [Controller] Thiết bị id={} deleted successfully, redirecting to /thietbi", id);
            return "redirect:/thietbi?success=Da xoa thiet bi thanh cong";
        } catch (IllegalArgumentException e) {
            log.error("===> [Controller] Error: {}", e.getMessage());
            return "redirect:/thietbi?error=" + e.getMessage();
        } catch (Exception e) {
            log.error("===> [Controller] Unexpected error deleting thiết bị id={}: {}", id, e.getMessage(), e);
            return "redirect:/thietbi?error=Loi khi xoa thiet bi";
        }
    }

    @GetMapping("/phong/{idPhong}")
    public String listByPhong(@PathVariable Integer idPhong, Model model) {
        log.info("===> [Controller] /thietbi/phong/{} called", idPhong);
        PhongHoc phong = phongRepo.findById(idPhong).orElse(null);
        if (phong == null) {
            log.error("===> [Controller] Phòng id={} không tồn tại", idPhong);
            return "redirect:/thietbi?error=Phong khong ton tai";
        }
        List<ThietBi> list = service.getByPhongHoc(idPhong);
        model.addAttribute("thietbis", list);
        model.addAttribute("phong", phong);
        return "phong_thietbi";
    }
}