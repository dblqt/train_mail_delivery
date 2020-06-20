package com.dblqt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
}
