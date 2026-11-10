package com.pbo.quiz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "operasi")
public class Operasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bilangan1", nullable = false)
    private Double bilangan1;

    @Column(name = "operator", nullable = false, length = 1)
    private String operator;

    @Column(name = "bilangan2", nullable = false)
    private Double bilangan2;

    // Tidak ada field "hasil" karena hasil dihitung di halaman HTML saja.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBilangan1() {
        return bilangan1;
    }

    public void setBilangan1(Double bilangan1) {
        this.bilangan1 = bilangan1;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getBilangan2() {
        return bilangan2;
    }

    public void setBilangan2(Double bilangan2) {
        this.bilangan2 = bilangan2;
    }
}