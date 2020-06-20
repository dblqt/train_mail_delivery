package com.dblqt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString
public class Train {
    private final String name;

    private final int capacity;

    private final List<Package> cargo = new ArrayList<>();

    @Setter
    private Node location;
}
