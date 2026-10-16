/** JPA entity representing the RexUserRoleId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RexUserRoleId implements Serializable {
    @Column(name = "rexuser_id", nullable = false)
    private Long rexuserId;

    @Column(name = "userrole_id", nullable = false)
    private Long userroleId;

    @Column(name = "activefrom", nullable = false)
    private java.time.LocalDate activeFrom;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RexUserRoleId that = (RexUserRoleId) o;
        return Objects.equals(rexuserId, that.rexuserId) &&
                Objects.equals(userroleId, that.userroleId) &&
                Objects.equals(activeFrom, that.activeFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rexuserId, userroleId, activeFrom);
    }
}
