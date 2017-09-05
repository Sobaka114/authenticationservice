package com.sasha.authenticationservice.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class UserEntity {
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public EncryptionType getEncryption() {
        return encryption;
    }

    public enum EncryptionType {
        AES
    }
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    @Enumerated(EnumType.STRING)
    private EncryptionType encryption;
}
