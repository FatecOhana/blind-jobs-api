package com.blindjobs.dto.command;

import com.blindjobs.dto.Payload;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpsertItemsCommand<T> {
    private final Payload<T> data;
}
