package com.ocado.basket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

class UtilsTest {

    @Test
    void bitMasks() {
        var expected = List.of(
                List.of(false, false, false),
                List.of(false, false, true),
                List.of(false, true, false),
                List.of(false, true, true),
                List.of(true, false, false),
                List.of(true, false, true),
                List.of(true, true, false),
                List.of(true, true, true)
        );

        Assertions.assertEquals(
                expected,
                Utils.bitMasks(3)
        );
    }

    @Test
    void applyMask() {
        var masks = List.of(
                List.of(false, false),
                List.of(false, true),
                List.of(true, false),
                List.of(true, true)
        );

        var input = List.of("a", "b");

        var expected = List.of(
                List.of(),
                List.of("b"),
                List.of("a"),
                List.of("a", "b")
        );

        Assertions.assertEquals(
                expected,
                masks.stream().map(mask -> Utils.applyMask(input, mask)).toList()
        );
    }

    @Test
    void reverseMap() {
        var map = Map.ofEntries(
                Map.entry("k1", List.of("v1", "v2", "v3")),
                Map.entry("k2", List.of("v1", "v2", "v4")),
                Map.entry("k3", List.of("v3", "v4"))
        );

        var expected = Map.ofEntries(
                Map.entry("v1", Set.of("k1", "k2")),
                Map.entry("v2", Set.of("k1", "k2")),
                Map.entry("v3", Set.of("k1", "k3")),
                Map.entry("v4", Set.of("k2", "k3"))
        );

        Assertions.assertEquals(
                expected,
                Utils.reverseMap(map)
        );

    }
}