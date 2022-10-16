package com.blindjobs.database.models.complement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "TB_ADDRESS")
public class Address {

    // Default class values
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Schema(description = "complete address", example = "Avenida Paulista, 123 - São Paulo-SP")
    @Column(nullable = false)
    private String name;

    @Schema(description = "unique identifier", example = "PAULISTA_123_SP")
    @Column(nullable = false, unique = true)
    private String identifierName;

    @Schema(description = "defines if the register is deleted. this tag allows you to retrieve possible excluded cases",
            defaultValue = "false")
    private Boolean isDeleted = Boolean.FALSE;


    @Schema(description = "cep identifier. With it it is possible to get all the address", example = "05541100")
    @Column(nullable = false)
    private String cep;

    @Schema(description = "optional address complement", example = "Bl 1, Apto 1512")
    private String complement;

}
