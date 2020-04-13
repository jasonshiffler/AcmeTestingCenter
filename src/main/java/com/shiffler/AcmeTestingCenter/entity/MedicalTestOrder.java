/*
 * Defines the parameters that make up an order for a medical test.
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
import java.time.OffsetDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class MedicalTestOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name="seq_generator", sequenceName = "medical_test_order_seq",
            initialValue = 100000000, allocationSize = 1)
    @Column(name="id", updatable = false)
    private Long id;

    @Version
    @Column(name="version")
    private Integer version;

    @CreationTimestamp
    @Column(name="created_date_time", updatable = false)
    private OffsetDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name="last_modified_date_time")
    private OffsetDateTime lastModifiedDateTime;

    @Length(min = 10, max = 10, message ="The field must be 10 characters long")
    @Column(name="test_code", updatable = false,nullable = false)
    private String testCode;

    @Column(name="test_status")
    @Enumerated(EnumType.STRING)  //Populates the database with the String value instead of a number
    private MedicalTestOrderStatusEnum testOrderStatusEnum;

    @Column(name="medical_test_result")
    @Enumerated(EnumType.STRING)
    private MedicalTestResultEnum medicalTestResultEnum;

}
