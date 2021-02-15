package com.khovaylo.surf.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * @author Pavel Khovaylo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "expression")
public class Expression {
    @Id
    @SequenceGenerator(name = "EXPRESSION_SEQ", sequenceName = "expression_seq", allocationSize = 1)
    @GeneratedValue(generator = "EXPRESSION_SEQ", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    Long id;

    @Basic
    @Column(name = "value", nullable = false, length = 254)
    String value;

    @Basic
    @Column(name = "result", nullable = false)
    Double result;

    @Column(name = "created", nullable = false)
    ZonedDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;
}