package com.pbo.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pbo.quiz.model.Operasi;
import com.pbo.quiz.repository.OperasiRepository;

@Service
public class OperasiService {

    private final OperasiRepository repository;

    public OperasiService(OperasiRepository repository) {
        this.repository = repository;
    }

    public List<Operasi> semua() {
        return repository.findAll(Sort.by("id"));
    }

    public Optional<Operasi> cari(Long id) {
        return repository.findById(id);
    }

    public Operasi simpan(Operasi operasi) {
        return repository.save(operasi);
    }

    public void hapus(Long id) {
        repository.deleteById(id);
    }
}