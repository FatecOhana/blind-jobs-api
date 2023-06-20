package com.blindjobs.dto.command;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class FindItemByParameterCommand {
    private final UUID id;
    private final String name;
    private final String uniqueKey;
    private final Object type;
    private final Boolean isDeleted;
}
