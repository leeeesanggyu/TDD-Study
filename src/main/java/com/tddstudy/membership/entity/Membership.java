package com.tddstudy.membership.entity;

import com.tddstudy.membership.util.MembershipKindType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String userId;

    @Column(nullable = false)
    private MembershipKindType kind;

    @Setter
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point;

    @CreationTimestamp
    @Column(nullable = false, length = 20, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(length = 20)
    private LocalDateTime updatedAt;
}
