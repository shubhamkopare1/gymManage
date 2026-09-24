package gym_management_backend.gymmanagement.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "diet_meals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String mealName;

    private String mealTime;

    @Column(length = 2000)
    private String foodItems;

    private Integer calories;

    private Double protein;

    private Double carbohydrates;

    private Double fats;

    @Column(length = 1000)
    private String instructions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_plan_id", nullable = false)
    private DietPlan dietPlan;
}