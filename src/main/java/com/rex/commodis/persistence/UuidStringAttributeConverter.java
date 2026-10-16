/** UuidStringAttributeConverter: part of the REX multi-tenant expense management platform. */
package com.rex.commodis.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

/**
 * Converts a String (Java) <-> UUID (DB) so we can keep String in our DTO/entity
 * while persisting to PostgreSQL UUID columns.
 */
@Converter(autoApply = false)
public class UuidStringAttributeConverter implements AttributeConverter<String, UUID> {

    @Override
    public UUID convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return null;
        }
        // Normalize and validate
        try {
            return UUID.fromString(attribute.trim());
        } catch (IllegalArgumentException ex) {
            // If invalid UUID string, surface a clear error
            throw new IllegalArgumentException("Invalid UUID string: '" + attribute + "'", ex);
        }
    }

    @Override
    public String convertToEntityAttribute(UUID dbData) {
        return dbData == null ? null : dbData.toString();
    }
}
