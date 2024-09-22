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
@Table(name="user_roles")
public class UserRole {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long role_id;

    private String name;

}
