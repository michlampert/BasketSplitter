package com.ocado.basket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Streams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.intersection;
import static com.ocado.basket.Utils.*;

public class BasketSplitter {

    protected static final Logger logger = LogManager.getLogger();

    private final Map<String, List<String>> config;
    private final Map<String, Set<String>> reversedConfig;

    private static Map<String, List<String>> readConfig(String absolutePathToConfigFile) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(absolutePathToConfigFile);

            return Streams.stream(objectMapper.readTree(file).fields())
                    .map(field -> {
                        String key = field.getKey();
                        List<String> value = Streams.stream(field.getValue().iterator())
                                .map(JsonNode::asText).toList();
                        return Map.entry(key, value);
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        } catch (IOException e) {
            logger.error("Wrong config file");
            return Map.of();
        }
    }

    public BasketSplitter(String absolutePathToConfigFile) {
        this(readConfig(absolutePathToConfigFile));
    }

    public BasketSplitter(Map<String, List<String>> config) {
        this.config = config;
        this.reversedConfig = reverseMap(config);
    }

    public Map<String, List<String>> getConfig() {
        return config;
    }

    private List<Set<String>> getAllDeliveryOptionSubsets() {
        List<String> options = toList(reversedConfig.keySet());
        List<List<Boolean>> masks = bitMasks(options.size());

        return masks.stream().map(mask -> toSet(applyMask(options, mask))).toList();
    }

    protected boolean checkCover(Set<String> options, List<String> items) {
        return options
                .stream()
                .flatMap(option -> reversedConfig.getOrDefault(option, Set.of()).stream())
                .collect(Collectors.toSet())
                .containsAll(items);
    }

    public List<Set<String>> getAllValidDeliveryOptionSubsets(List<String> items) {
        List<Set<String>> subsets = getAllDeliveryOptionSubsets()
                .stream()
                .filter(options -> checkCover(options, items))
                .toList();

        int minimalSize = subsets.stream().map(Set::size).reduce((x, y) -> x < y ? x : y).orElse(0);

        return subsets.stream().filter(subset -> subset.size() == minimalSize).toList();
    }

    protected Optional<Pair<String, List<String>>> getLargestDeliveryOption(List<String> items, Set<String> options) {
        return options.stream()
                .map(option -> new Pair<>(option, toList(intersection(reversedConfig.get(option), toSet(items)))))
                .reduce((p1, p2) -> p1.getValue1().size() > p2.getValue1().size() ? p1 : p2);
    }

    private Map<String, List<String>> split(List<String> items, Set<String> options) {
        if (items.isEmpty()) return Map.of();

        return getLargestDeliveryOption(items, options)
                .map(pair -> {
                    List<String> remainingItems = toList(difference(toSet(items), toSet(pair.getValue1())));
                    Set<String> remainingOptions = toSet(difference(options, Set.of(pair.getValue0())));

                    Map<String, List<String>> remainingSplit = split(remainingItems, remainingOptions);

                    return union(Map.ofEntries(toMapEntry(pair)), remainingSplit);
                })
                .orElse(Map.of());
    }

    public Map<String, List<String>> split(List<String> items) {
        return getAllValidDeliveryOptionSubsets(items)
                .stream()
                .map(subset -> split(items, subset))
                .map(map -> new Pair<>(map, map.values().stream().mapToInt(List::size).min().orElse(0)))
                .reduce((p1, p2) -> p1.getValue1() > p2.getValue1() ? p1 : p2)
                .map(Pair::getValue0)
                .orElse(Map.of());
    }
}