package com.joanlica.ecommerce.user.model;

import com.joanlica.ecommerce.auth.model.UserAuth;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "user_profiles")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    // Relationship with UserAuth
    @OneToOne(optional = false)
    @JoinColumn(name = "user_auth_id", nullable = false, unique = true)
    private UserAuth userAuth;
}