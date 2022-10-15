package com.blindjobs.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UtilsOperation {
    private static final Logger logger = LoggerFactory.getLogger(UtilsOperation.class);

    public static Set<UUID> convertStringsToUUIDs(Set<String> uuids) {
        return uuids.stream().map(UtilsOperation::convertStringToUUID).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public static UUID convertStringToUUID(String uuid) {
        return UtilsValidation.isNullOrEmpty(uuid) || !dataIsUUID(uuid) ? null : UUID.fromString(uuid);
    }

    public static boolean dataIsUUID(String data) {
        try {
            if (UtilsValidation.isNullOrEmpty(data)) {
                logger.warn("data empty is not UUID type");
                return false;
            }
            return !UtilsValidation.isNullOrEmpty(UUID.fromString(data).toString());
        } catch (Exception ex) {
            logger.warn(String.format("data [%s] is not UUID type", data), ex);
            return false;
        }
    }
}
