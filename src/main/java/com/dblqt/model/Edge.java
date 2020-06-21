package com.dblqt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class Edge {
    private final String name;

    private final Node start;

    private final Node end;

    private final int distance;
}
