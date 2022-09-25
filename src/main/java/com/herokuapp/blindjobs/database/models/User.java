package com.herokuapp.blindjobs.database.models;

import com.herokuapp.blindjobs.dto.types.DocumentTypes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {

    // Unique Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    // Personal Values
    @Schema(description = "name's user", example = "Gabriel")
    @Column(nullable = false)
    private String name;
    @Schema(description = "user's username", example = "gabriel_1513")
    @Column(unique = true, nullable = false)
    private String username;
    @Schema(description = "user's phone", example = "119284928470")
    private String contactNumber;


    // Acess Data
    @Schema(description = "user's email", example = "gabriel@email.com")
    @Column(unique = true, nullable = false)
    private String email;
    @Schema(description = "user's password. This password will be encrypted in the database", example = "gabriel852109712")
    @Column(nullable = false)
    private String password;

    // Documents used in Register (CPF, CNPJ, RNE, RG)
    @Schema(description = "user's document value. Only the numbers will be saved in the database", example = "12346578901")
    @Column(nullable = false)
    private String documentValue;
    @Schema(description = "user's document type. Defines the type of document inserted", example = "CPF")
    @Column(nullable = false)
    private DocumentTypes documentType;

    // User Address Data
//    private PersonalAddress address;

    // User Skills and Qualifications
//    private List<Skills> skills;

    // User Professional History (Jobs, Ongs, etc)
//    private List<WorkExperience> workExperiences;

    // User sStudent Qualification
//    private List<SchoolQualification> schoolQualification;
}
