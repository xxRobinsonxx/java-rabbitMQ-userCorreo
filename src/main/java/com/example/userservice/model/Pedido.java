package com.example.userservice.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.time.LocalDate;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nro;

    @ManyToOne(fetch = FetchType.EAGER) // trae los datos del usuario con cada pedido
    @JoinColumn(name = "user_id", nullable = false) // FK en la tabla
    private User user;

    private LocalDate date;

    private String ruc;

    private String address;

    public Pedido() {}

    public Pedido(String nro, User user, LocalDate date, String ruc, String address) {
        this.nro = nro;
        this.user = user;
        this.date = date;
        this.ruc = ruc;
        this.address = address;
    }

    // Getters y Setters...

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNro() { return nro; }
    public void setNro(String nro) { this.nro = nro; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
