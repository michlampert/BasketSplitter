package com.ocado.basket;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        BasketSplitter bs = new BasketSplitter(Map.ofEntries(
                Map.entry("Carrots (1kg)", List.of("Express Delivery", "Click&Collect")),
                Map.entry("Cold Beer (330ml)", List.of("Express Delivery")),
                Map.entry("Steak (300g)", List.of("Express Delivery", "Click&Collect")),
                Map.entry("AA Battery (4 Pcs.)", List.of("Express Delivery", "Courier")),
                Map.entry("Espresso Machine", List.of("Courier", "Click&Collect")),
                Map.entry("Garden Chair", List.of("Courier"))
        ));

        var items = List.of("Steak (300g)", "Carrots (1kg)", "Cold Beer (330ml)", "AA Battery (4 Pcs.)", "Espresso " +
                "Machine", "Garden Chair");

        var res = bs.split(items);
        System.out.println(res);
    }
}
