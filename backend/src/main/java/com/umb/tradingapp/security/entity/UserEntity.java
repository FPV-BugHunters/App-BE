package com.umb.tradingapp.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.ManyToMany;

import java.util.List;
import java.util.Set;

import com.umb.tradingapp.entity.BalanceHistoryEntity;
import com.umb.tradingapp.entity.UserPortfolioEntity;
import com.umb.tradingapp.service.BalanceHistoryService;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column()
    private String firstName;
   
    @Column()
    private String lastName;
    
    @Column() 
    private String email;
    
    @Column()
    private String phoneNumber;

    @Column()
    private Double balance;

    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    @OneToMany(mappedBy = "user")
    private Set<UserPortfolioEntity> userPortfolios;

    
}
