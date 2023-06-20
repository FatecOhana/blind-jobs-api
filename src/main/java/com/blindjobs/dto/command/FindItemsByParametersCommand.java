package com.blindjobs.dto.command;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
public class FindItemsByParametersCommand {
    private final Set<UUID> id;
    private final Set<String> name;
    private final Set<String> uniqueKey;
    private final Object type;
    private final Boolean isDeleted;
}
