/** JPA entity representing the RexUserRights table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.Entity;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Getter
@Setter
@Entity
@Immutable
@Subselect("select * from rexbase.rexuser_rights")
public class RexUserRights {

    // Since a view might not have a single unique physical ID column,
    // we use a composite of columns that make a row unique.
    // Based on "SELECT DISTINCT rexuser_id, listname, rightcode",
    // the combination of rexuser_id and rightcode is likely unique per user.
    // However, for simplicity in JPA read-only views, using @Id on one column (like
    // rightcode or combination) is required.
    // Let's use rightcode as ID for now, but in reality, composite key would be
    // better if one user has multiple rights.
    // Wait, (rexuser_id, rightcode) is unique. We need a composite ID class.

    @jakarta.persistence.EmbeddedId
    private RexUserRightsId id;

    @Column(name = "listname", insertable = false, updatable = false)
    private String listname;

    // Helper getters for convenience
    public Long getRexuserId() {
        return id != null ? id.getRexuserId() : null;
    }

    public String getRightcode() {
        return id != null ? id.getRightcode() : null;
    }

}
