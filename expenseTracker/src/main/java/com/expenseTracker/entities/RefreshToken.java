package com.expenseTracker.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="tokens")
public class RefreshToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;

    private Instant expiry_time;


    @OneToOne
    @JoinColumn(name="id" , referencedColumnName = "user_id")
    private UserInfo userInfo;

}
