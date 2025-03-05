package com.expenseTracker.entities;


import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="user_roles", indexes = @Index(name = "idx_role_id", columnList = "role_id"))  // Create index on role_id
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long role_id;  // This column should be indexed automatically as it's the primary key

    private String name;
}
