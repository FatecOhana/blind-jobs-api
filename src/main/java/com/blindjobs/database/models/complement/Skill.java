package com.blindjobs.database.models.complement;

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
@Entity(name = "TB_SKILL")
public class Skill {

    // Unique Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Schema(description = "defines if a job is deleted. This tag allows you to retrieve possible excluded cases", defaultValue = "false")
    private Boolean isDeleted = Boolean.FALSE;

    // Skill Description
    @Schema(description = "title of skill", example = "Java 8")
    @Column(nullable = false)
    private String title;

    @Schema(description = "unique name to identifier a skill", example = "JAVA8")
    @Column(nullable = false, unique = true)
    private String identifierName;

    @Schema(description = "generic description of skill. It's only registred by admins (DEV and Enterprises)",
            example = "The most popular Java Version")
    @Column(nullable = false)
    private String description;


}
