package com.blindjobs.database.models.complement;

import com.blindjobs.database.models.base.BaseEntity;
import com.blindjobs.database.models.entities.Job;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "TB_ADDRESS")
public class Address extends BaseEntity {

    @Schema(description = "cep identifier. With it it is possible to get all the address", example = "05541100")
    @Column(nullable = false)
    private String cep;

    @Schema(description = "optional address complement", example = "Bl 1, Apto 1512")
    private String complement;

    public Address(UUID id, String name, String identifierName, Boolean isDeleted, String cep, String complement) {
        super(id, name, identifierName, isDeleted);
        this.cep = cep;
        this.complement = complement;
    }
}
