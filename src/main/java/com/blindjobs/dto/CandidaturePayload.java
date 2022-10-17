package com.blindjobs.dto;

import com.blindjobs.database.models.entities.Job;
import com.blindjobs.database.models.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CandidaturePayload {
    @Schema(description = "user to be candidate in job")
    private User user;

    @Schema(description = "job values")
    private Job job;
}
