package com.blindjobs.database.models.base;

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
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Schema(description = "name of entity", example = "Generic name of Entity")
    @Column(nullable = false)
    private String name;

    @Schema(description = "unique name of entity", example = "Entity-UnIqUe_USERNAME")
    @Column(nullable = false, unique = true)
    private String identifierName;

    @Schema(description = "defines if the register is deleted. this tag allows you to retrieve possible excluded cases",
            defaultValue = "false")
    private Boolean isDeleted = Boolean.FALSE;
}
