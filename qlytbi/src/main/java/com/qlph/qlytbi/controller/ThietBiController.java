package com.qlph.qlytbi.controller;

import com.qlph.qlytbi.entity.ThietBi;
import com.qlph.qlytbi.repository.PhongHocRepository;
import com.qlph.qlytbi.service.ThietBiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/thietbi")
public class ThietBiController {

    @Autowired
    private ThietBiService service;

    @Autowired
    private PhongHocRepository phongRepo;

    @GetMapping
    public String list(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<ThietBi> list = (keyword == null || keyword.isEmpty())
                ? service.getAll()
                : service.search(keyword);
        model.addAttribute("thietbis", list);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("thietbi", new ThietBi());
        model.addAttribute("phongs", phongRepo.findAll());
        return "form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ThietBi tb) {
        service.save(tb);
        return "redirect:/thietbi";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("thietbi", service.getById(id));
        model.addAttribute("phongs", phongRepo.findAll());
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/thietbi";
    }
}
