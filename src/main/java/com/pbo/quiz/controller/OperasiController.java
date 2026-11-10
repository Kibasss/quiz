package com.pbo.quiz.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pbo.quiz.model.Operasi;
import com.pbo.quiz.service.OperasiService;

@Controller
public class OperasiController {

    private static final List<String> OPERATOR_VALID = List.of("+", "-", "*", "/");

    private final OperasiService service;

    public OperasiController(OperasiService service) {
        this.service = service;
    }

    // READ: tampilkan halaman dengan form kosong + daftar data
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("operasi", new Operasi());
        model.addAttribute("daftar", service.semua());
        return "index";
    }

    // Tombol EDIT: tampilkan halaman yang sama, form terisi data lama
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Operasi operasi = service.cari(id).orElse(null);
        if (operasi == null) {
            return "redirect:/";
        }
        model.addAttribute("operasi", operasi);
        model.addAttribute("daftar", service.semua());
        return "index";
    }

    // CREATE & UPDATE: kalau id kosong -> data baru, kalau ada id -> ubah data
    @PostMapping("/simpan")
    public String simpan(@ModelAttribute("operasi") Operasi operasi,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {

        validasi(operasi, result);

        if (result.hasErrors()) {
            model.addAttribute("daftar", service.semua());
            return "index";
        }

        boolean dataBaru = operasi.getId() == null;
        service.simpan(operasi);
        redirect.addFlashAttribute("pesan",
                dataBaru ? "Data berhasil disimpan." : "Data berhasil diubah.");
        return "redirect:/";
    }

    // DELETE
    @PostMapping("/hapus/{id}")
    public String hapus(@PathVariable Long id, RedirectAttributes redirect) {
        service.hapus(id);
        redirect.addFlashAttribute("pesan", "Data berhasil dihapus.");
        return "redirect:/";
    }

    // ---------- Validasi sederhana ----------

    private void validasi(Operasi o, BindingResult result) {
        cekAngka(o.getBilangan1(), "bilangan1", "Bilangan 1", result);
        cekAngka(o.getBilangan2(), "bilangan2", "Bilangan 2", result);

        if (o.getOperator() == null || !OPERATOR_VALID.contains(o.getOperator())) {
            result.rejectValue("operator", "tidakValid", "Operator tidak valid.");
        }

        // Pembagian dengan 0 tidak boleh disimpan
        if ("/".equals(o.getOperator())
                && o.getBilangan2() != null
                && o.getBilangan2() == 0
                && !result.hasFieldErrors("bilangan2")) {
            result.rejectValue("bilangan2", "bagiNol", "Pembagi tidak boleh 0.");
        }
    }

    private void cekAngka(Double nilai, String field, String nama, BindingResult result) {
        if (result.hasFieldErrors(field)) {
            return; // sudah ada error (misalnya diisi huruf)
        }
        if (nilai == null) {
            result.rejectValue(field, "kosong", nama + " wajib diisi.");
        } else if (!Double.isFinite(nilai)) {
            result.rejectValue(field, "tidakValid", nama + " harus berupa angka biasa.");
        }
    }
}