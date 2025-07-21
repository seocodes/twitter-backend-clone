package com.seocodes.spring_security.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gerar automaticamente
    @Column(name = "role_id")
    private Long roleId;  // Não vai ser exposto publicamente esse ID, pode ser Long

    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Values{  // Enum para definir os diferentes tipos de Roles que podemos ter na aplicação
        ADMIN(1L),  // Administrador
        BASIC(2L);  // Usuário normal

        long roleId;   // Para identificar unicamente cada role - mais fácil para persistir no banco/services

        Values(long roleId) {
            this.roleId = roleId;
        }

        public long getRoleId() {
            return roleId;
        }
    }
}
