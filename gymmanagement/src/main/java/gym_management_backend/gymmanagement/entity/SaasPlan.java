package gym_management_backend.gymmanagement.entity;


import gym_management_backend.gymmanagement.entity.enums.SaasPlanType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "saas_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaasPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private SaasPlanType planType;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer durationInDays;

    @Column(nullable = false)
    private Integer maxMembers;

    @Column(nullable = false)
    private Integer maxTrainers;

    @Column(nullable = false)
    private boolean active = true;
}