package com.dblqt.common;

import java.util.Set;
import java.util.stream.Collectors;

import com.dblqt.model.Package;

public class PrettyFormat {

    public static String formatPackages(final Set<Package> packages) {
        return packages.stream().map(Package::getName).collect( Collectors.joining( "," ) );
    }
}
