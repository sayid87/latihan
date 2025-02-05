package com.latihan.latihan.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntities {

    @Id
    @Column(name = "id_user")
    private int idUser;
    private String email;
    private String password;
    private String nama;
    private String telp;
    private String foto;

}
