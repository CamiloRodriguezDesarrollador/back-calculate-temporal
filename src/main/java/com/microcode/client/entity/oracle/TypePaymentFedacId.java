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
public class TypePaymentFedacId implements Serializable {

    private String typePaymentName;
    private String typeDocument;
    private Long document;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypePaymentFedacId that)) return false;
        return Objects.equals(typePaymentName, that.typePaymentName) &&
                Objects.equals(typeDocument, that.typeDocument) &&
                Objects.equals(document, that.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typePaymentName, typeDocument, document);
    }
}
