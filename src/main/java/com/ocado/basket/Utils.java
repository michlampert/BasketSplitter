package com.ocado.basket;

import com.google.common.collect.Streams;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    static public List<List<Boolean>> bitMasks(int size) {
        if (size <= 0) return List.of(List.of());

        return bitMasks(size - 1).stream().flatMap(list -> Stream.of(
                Stream.concat(list.stream(), Stream.of(false)).toList(),
                Stream.concat(list.stream(), Stream.of(true)).toList()
        )).toList();
    }

    static private <T> Optional<T> applyBit(T elem, Boolean bit) {
        return bit ? Optional.of(elem) : Optional.empty();
    }

    static public <T> List<T> applyMask(List<T> collection, List<Boolean> mask) {
        return Streams
                .zip(collection.stream(), mask.stream(), Utils::applyBit)
                .flatMap(Optional::stream)
                .toList();
    }

    static public <A, B> Map<B, Set<A>> reverseMap(Map<A, List<B>> map) {
        return map
                .entrySet()
                .stream()
                .flatMap(entry -> entry
                        .getValue()
                        .stream()
                        .map(value -> new Pair<>(entry.getKey(), value))
                )
                .collect(Collectors.groupingBy(
                        Pair::getValue1,
                        Collectors.mapping(Pair::getValue0, Collectors.toSet()))
                );
    }

    static public <A> Set<A> toSet(Collection<A> collection) {
        return collection.stream().collect(Collectors.toSet());
    }

    static public <A> List<A> toList(Collection<A> collection) {
        return collection.stream().toList();
    }

    static public <A, B> Map.Entry<A, B> toMapEntry(Pair<A, B> pair) {
        return Map.entry(pair.getValue0(), pair.getValue1());
    }

    static public <A, B> Map<A, B> union(Map<A, B> map1, Map<A, B> map2) {
        return Streams
                .concat(map1.entrySet().stream(), map2.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
