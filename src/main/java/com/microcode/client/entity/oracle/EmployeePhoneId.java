package com.microcode.client.entity.oracle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePhoneId implements Serializable {

    private Long eplNd;
    private String tdcTd;
    private Long number;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeePhoneId that)) return false;
        return Objects.equals(eplNd, that.eplNd) &&
                Objects.equals(tdcTd, that.tdcTd) &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eplNd, tdcTd, number);
    }
}
