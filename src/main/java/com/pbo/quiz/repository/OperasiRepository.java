package com.pbo.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pbo.quiz.model.Operasi;

public interface OperasiRepository extends JpaRepository<Operasi, Long> {
    // Method save, findAll, findById, deleteById sudah disediakan JpaRepository
}