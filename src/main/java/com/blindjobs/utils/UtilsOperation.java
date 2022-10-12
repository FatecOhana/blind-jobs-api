package com.blindjobs.utils;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UtilsOperation {
    public static Set<UUID> convertStringsToUUIDs(Set<String> uuids) {
        return uuids.stream().map(UtilsOperation::convertStringToUUID).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public static UUID convertStringToUUID(String uuid) {
        return UtilsValidation.isNullOrEmpty(uuid) ? null : UUID.fromString(uuid);
    }
}
