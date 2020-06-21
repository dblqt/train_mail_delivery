package com.dblqt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
@ToString
public class Package {
    private final String name;

    private final Node destination;

    private final int weight;

    @Getter
    @Setter
    private boolean delivered;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return name.equals(aPackage.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
