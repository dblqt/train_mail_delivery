package com.dblqt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class Move {
    private final Train train;
    private final Node start;
    private final Path destination;
    private final int startTime;
    private final Edge segment;
    private final int expectedArrivalTime;

    private final Set<Package> load = new HashSet<>();
    private final Set<Package> drop = new HashSet<>();

}
