/*
Defines user objects which will be used for authentication and authorization
 */

package com.shiffler.AcmeTestingCenter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
//Can't have a table named user in postgres
@Table(name="user_entity", uniqueConstraints = @UniqueConstraint(name = "username_must_be_unique"
        ,columnNames = "username"))
public class User {

    @Id
    @GeneratedValue
    @Column(name="id", columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private boolean active;

    @Column
    private String roles;

    @Column
    private String permissions;

    public User(String username, String password, String roles, String permissions){
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
        this.active = true;
    }
}
