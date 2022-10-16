package com.blindjobs.database.models.entities;

import com.blindjobs.utils.UtilsValidation;
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
@Entity(name = "TB_STUDENT")
public class Student {

    // Default class values
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

    // User Skills and Qualifications
//    private List<Skills> skills;

    // User Professional History (Jobs, Ongs, etc)
//    private List<WorkExperience> workExperiences;

    // User sStudent Qualification
//    private List<SchoolQualification> schoolQualification;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (UtilsValidation.isNull(obj) || getClass() != obj.getClass() || UtilsValidation.isNull(id))
            return false;
        Student that = (Student) obj;
        return id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return UtilsValidation.isNull(id) ? 0 : id.hashCode();
    }

}
