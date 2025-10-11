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
        } catch (IllegalArgumentException e) {
            log.error("===> [Controller] Error: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("phongs", phongRepo.findAll());
            return "form";
        }
        return "redirect:/thietbi";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        log.info("===> [Controller] Editing thiết bị id={}", id);
        model.addAttribute("thietbi", service.getById(id));
        model.addAttribute("phongs", phongRepo.findAll());
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        log.warn("===> [Controller] Deleting thiết bị id={}", id);
        service.delete(id);
        return "redirect:/thietbi";
    }

    @GetMapping("/phong/{idPhong}")
    public String listByPhong(@PathVariable Integer idPhong, Model model) {
        log.info("===> [Controller] /thietbi/phong/{} called", idPhong);
        List<ThietBi> list = service.getByPhongHoc(idPhong);
        model.addAttribute("thietbis", list);
        model.addAttribute("phong", phongRepo.findById(idPhong).orElse(null));
        return "phong_thietbi";
    }
}