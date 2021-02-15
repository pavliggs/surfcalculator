package com.khovaylo.surf.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

/**
 * @author Pavel Khovaylo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User {
    @Id
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(generator = "USER_SEQ", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    Long id;

    @Basic
    @Column(name = "user_name", nullable = false, length = 100)
    String userName;

    @Basic
    @Column(name = "password", nullable = false, length = 254)
    String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Expression> expressions;
}