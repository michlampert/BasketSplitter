package com.ocado.basket;

import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ocado.basket.Utils.toSet;

class BasketSplitterTest {

    private BasketSplitter bs;

    @BeforeEach
    void setUp() {
        bs = new BasketSplitter(Map.ofEntries(
                Map.entry("Carrots (1kg)", List.of("Express Delivery", "Click&Collect")),
                Map.entry("Cold Beer (330ml)", List.of("Express Delivery")),
                Map.entry("Steak (300g)", List.of("Express Delivery", "Click&Collect")),
                Map.entry("AA Battery (4 Pcs.)", List.of("Express Delivery", "Courier")),
                Map.entry("Espresso Machine", List.of("Courier", "Click&Collect")),
                Map.entry("Garden Chair", List.of("Courier"))
        ));
    }

    @Test
    void checkConfigParsing() {
        var bs2 = new BasketSplitter("src/test/resources/config.json");

        Assertions.assertEquals(bs.getConfig(), bs2.getConfig());
    }

    @Test
    void checkCover() {
        Assertions.assertTrue(bs.checkCover(
                Set.of("Courier"),
                List.of("Espresso Machine", "Garden Chair")
        ));
        Assertions.assertFalse(bs.checkCover(
                Set.of("Courier"),
                List.of("Steak (300g)")
        ));
    }

    @Test
    void getAllValidDeliverySubsets() {
        Assertions.assertTrue(bs.getAllValidDeliveryOptionSubsets(List.of("Espresso Machine", "Garden Chair")).contains(Set.of("Courier")));
        Assertions.assertFalse(
                bs.getAllValidDeliveryOptionSubsets(List.of("Espresso Machine", "Garden Chair"))
                        .contains(Set.of("Courier", "Click&Collect"))
        );
        Assertions.assertFalse(
                bs.getAllValidDeliveryOptionSubsets(List.of("Espresso Machine", "Garden Chair"))
                        .contains(Set.of("Express Delivery", "Click&Collect"))
        );
    }

    @Test
    void getLargestDeliveryOption() {
        var expected = new Pair<>("Courier", List.of("Espresso Machine", "Garden Chair"));

        var result1 = bs.getLargestDeliveryOption(
                List.of("Espresso Machine", "Garden Chair"),
                Set.of("Courier", "Click&Collect")
        ).get();

        Assertions.assertEquals(expected.getValue0(), result1.getValue0());
        Assertions.assertEquals(toSet(expected.getValue1()), toSet(result1.getValue1()));

        var result2 = bs.getLargestDeliveryOption(
                List.of("Espresso Machine", "Garden Chair", "Cold Beer (330ml)"),
                Set.of("Courier", "Express Delivery")
        ).get();

        Assertions.assertEquals(expected.getValue0(), result2.getValue0());
        Assertions.assertEquals(toSet(expected.getValue1()), toSet(result2.getValue1()));
    }

    @Test
    void split() {
        var result = bs.split(List.of("Steak (300g)", "Carrots (1kg)", "Cold Beer (330ml)", "AA Battery (4 Pcs.)",
                "Espresso Machine", "Garden Chair"));

        var expected = Map.ofEntries(
                Map.entry("Express Delivery", List.of("Cold Beer (330ml)", "Steak (300g)", "AA Battery (4 Pcs.)",
                        "Carrots (1kg)")),
                Map.entry("Courier", List.of("Garden Chair", "Espresso Machine"))
        );

        Assertions.assertEquals(
                expected.entrySet().stream().map(e -> Map.entry(e.getKey(), toSet(e.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                result.entrySet().stream().map(e -> Map.entry(e.getKey(), toSet(e.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }
}