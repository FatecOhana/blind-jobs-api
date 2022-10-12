package com.blindjobs.database.models.entities;

import com.blindjobs.dto.types.DocumentTypes;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Enterprise {

    // Unique Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    // Personal Values
    @Schema(description = "complete name of enterprise", example = "Nestl CIA Ilimited and Working")
    @Column(nullable = false)
    private String name;

    @Schema(description = "resumed name of enterprise", example = "Nestl CIA")
    @Column(nullable = false, unique = true)
    private String username;

    @Schema(description = "enterprise's phone", example = "119284928470")
    private String contactNumber;

    @Schema(description = "enterprise's public contact email", example = "company@somecompany.com.ua")
    private String contactEmail;

    // Acess Data
    @Schema(description = "enterprise private email", example = "comapnyprivate@somecompany.com")
    @Column(unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String email;

    @Schema(description = "enterprise's password. This password will be encrypted in the database", example = "enterprise852109712")
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // Documents used in Register (CPF, CNPJ, RNE, RG)
    @Schema(description = "enterprise's document value. Only the numbers will be saved in the database", example = "12346578901")
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String documentValue;

    @Schema(description = "enterprise's document type. Defines the type of document inserted", example = "CNPJ")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentTypes documentType;

    @Schema(description = "defines if a enterprise is deleted. this tag allows you to retrieve possible excluded cases", defaultValue = "false")
    private Boolean isDeleted = Boolean.FALSE;

    // User Address Data
//    private PersonalAddress address;


    @JsonManagedReference
    @OneToMany(mappedBy = "enterprise")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Job> jobs = new HashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (id == null || obj == null || getClass() != obj.getClass())
            return false;
        Enterprise that = (Enterprise) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
