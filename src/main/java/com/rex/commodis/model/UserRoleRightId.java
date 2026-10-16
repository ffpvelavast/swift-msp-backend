/** JPA entity representing the UserRoleRightId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
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
public class UserRoleRightId implements Serializable {
    @Column(name = "userrole_id", nullable = false)
    private Long userroleId;

    @Column(name = "userright_id", nullable = false)
    private Long userrightId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserRoleRightId that = (UserRoleRightId) o;
        return Objects.equals(userroleId, that.userroleId) && Objects.equals(userrightId, that.userrightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userroleId, userrightId);
    }
}
