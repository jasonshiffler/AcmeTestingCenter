/*
Defines user objects which will be used for authentication and authorization
 */

package com.shiffler.AcmeTestingCenter.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter //Using these instead of @Data. The toString generated from Lombok generates a circular reference which causes
        //a problem
@Setter
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

    @ManyToOne
    @JoinColumn(name="org_id", nullable = false)
    private Organization organization;


    public User(String username, String password, String roles, String permissions, Organization organization){
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
        this.active = true;
        this.organization = organization;
    }
}
