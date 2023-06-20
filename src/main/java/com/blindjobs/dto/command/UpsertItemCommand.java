package com.blindjobs.dto.command;

import com.blindjobs.dto.Payload;
import com.blindjobs.dto.SingleItemPayload;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpsertItemCommand<T> {
    private final SingleItemPayload<T> data;
}
