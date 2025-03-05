package com.expenseTracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @Column(name = "user_id")
    private String user_id;
    @Column(name = "user_name")
    private String username;
    @Column(name = "pass_word")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", // name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // column referring to UserInfo
            inverseJoinColumns = @JoinColumn(name = "role_id") // column referring to UserRole
    )
    private Set<UserRole> roles = new HashSet<>();


}
