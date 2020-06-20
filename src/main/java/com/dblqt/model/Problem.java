package com.dblqt.model;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Problem {
    private final Map<String, Node> nodeMap = new HashMap<>();

    private final List<Edge> edges = new ArrayList<>();

    private final List<Train> trains = new ArrayList<>();

    private final List<Package> packages = new ArrayList<>();

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public boolean undeliveredPackages() {
        return packages.stream().map(Package::isDelivered).anyMatch(x -> !x);
    }
}
