//Represents the organization that submitted the test

package com.shiffler.AcmeTestingCenter.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter //Using these instead of @Data. The toString generated from Lombok generates a circular reference which causes
        //a problem
@Setter
@Table(name="organization", uniqueConstraints = @UniqueConstraint(name = "organization_name_must_be_unique"
        ,columnNames = "organization_name"))
public class Organization {

    @Id
    @GeneratedValue
    @Column(name="id", columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(name="organization_name", nullable = false)
    private String organizationName;

    @CreationTimestamp
    @Column(name="created_date_time", updatable = false)
    private OffsetDateTime createdDateTime;

    @OneToMany(mappedBy="organization")
    private Set<User> users;

}
