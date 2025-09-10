package com.mawkly.auth_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity //marks this class as a JPA entity, meaning it will be mapped to a database table.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;


    /**
     *
     * @return String, user info.
     * TODO: change later to not show sensitive stuff
     */
    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + getUsername() + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}