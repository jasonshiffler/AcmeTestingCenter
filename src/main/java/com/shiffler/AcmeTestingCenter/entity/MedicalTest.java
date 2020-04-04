/**
 * Defines the parameters that make up a one of the medical tests that is available for order.
 */

package com.shiffler.AcmeTestingCenter.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MedicalTest {


    @Id
    @GeneratedValue//(generator = "UUID")
    @Column(name="id", columnDefinition = "uuid")// updatable = false,nullable = false)
    private UUID id;

    @Version
    @Column(name="version")
    private Integer version;

    @CreationTimestamp
    @Column(name="created_date_time")//, //updatable = false)
    //@Null
    private OffsetDateTime createdDateTime;

    @UpdateTimestamp
    //@Null
    @Column(name="last_modified_date_time")
    private OffsetDateTime lastModifiedDateTime;

    @Size(min=3, max =32)
    @Column(name="test_name")
    private String testName;

    @Min(value=0)
    @Max(value=9999)
    @Column(name="quantity_on_hand")
    private Integer quantityOnHand;

    @Min(value=0)
    @Max(value=9999)
    @Column(name="min_on_hand")
    private Integer minOnHand;

    @Min(value=0)
    @Max(value=9999)
    @Column(name="quantity_to_order")
    private Integer quantityToOrder;

    @Length(min = 10, max = 10, message ="The field must be 10 characters long")
    @Column(name="test_code")
    private String testCode;




}
