package com.dblqt.model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@ToString
public class Train {
    private final String name;

    @NonNull
    private int capacity;

    @Getter(AccessLevel.PRIVATE)
    private final Set<Package> cargo = new HashSet<>();

    @Setter
    private Voyage voyage;

    @Setter
    private Node location;

    public Set<Package> deliverPackages(final Node location) {
        final var delivered = new HashSet<Package>();

        for (var pkg: getCargo()) {
            if (pkg.getDestination().equals(location)) {
                pkg.setDelivered(true);
                delivered.add(pkg);
                capacity += pkg.getWeight();
            }
        }

        cargo.removeAll(delivered);

        return delivered;
    }

    public void loadPackage(final Package pkg) {
        if (pkg.getWeight() > capacity) {
            throw new RuntimeException("Tried to overload the train.");
        }

        cargo.add(pkg);
        capacity -= pkg.getWeight();
    }
}
