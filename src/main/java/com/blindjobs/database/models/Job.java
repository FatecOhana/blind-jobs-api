package com.blindjobs.database.models;

import com.blindjobs.dto.types.ContractType;
import com.blindjobs.dto.types.DayPeriod;
import com.blindjobs.dto.types.WorkModel;
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
public class Job {

    // Unique Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Schema(description = "defines if a job is deleted. This tag allows you to retrieve possible excluded cases", defaultValue = "false")
    private Boolean isDeleted = Boolean.FALSE;


    // Job Description
    @Schema(description = "title of job", example = "Decola 2022 -> Dev Frontend React Junior")
    @Column(nullable = false)
    private String title;

    @Schema(description = "resumed name of job", example = "NEST-FRONTEND_JR_2022")
    @Column(nullable = false, unique = true)
    private String identifierName;

    @Schema(description = "general description to company, job and chalanges",
            example = "a company that develops innovation projects, focusing on the vision of a competitive job market")
    @Column(nullable = false)
    private String description;

    // Work Specifications
    @Schema(description = "period of working", defaultValue = "AFTERNOON")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayPeriod periodJob;

    @Schema(description = "model of working", defaultValue = "HOME_OFFICE")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkModel workModel;

    @Schema(description = "type of job contract", defaultValue = "CLT")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractType contractType;


    @Schema(description = "weekly working hours", defaultValue = "CLT")
    @Column(nullable = false, precision = 2)
    private Double weekHours;

    @Schema(description = "number of working days", defaultValue = "CLT")
    @Column(nullable = false, precision = 2)
    private Double daysInWeek;


    // Job offers
    @Schema(description = "sets the minimum wage", defaultValue = "1250.00")
    @Column(precision = 2)
    private Double startSalaryRange;

    @Schema(description = "sets the max wage", defaultValue = "3000.00")
    @Column(precision = 2)
    private Double limitSalaryRange;

//    private Set<Benefits> benefits;

//    private Set<Skills> requiredSkills;

//    private Set<UserModel> userApplications;
//    private Enterprise userApplications;
//    private PersonalAddress address;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (id == null || obj == null || getClass() != obj.getClass())
            return false;
        Job that = (Job) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
