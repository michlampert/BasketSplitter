# BasketSplitter

Simple algorithm which can be used to split items in cart in optimal groups of product, to minimalize needed delivery
options.

## How does it works?

Since number of possible delivery options `k` is relatively small comparing to number of items `n` (2<sup>k</sup> ~ n),
algorithm checks all possible subsets of delivery options, and choose which of them can cover all requirements.
Then it choose the best options searching which subset can have have the biggest group of products.

## What is project contains?

Files inside `src/main/java/com/ocado/basket` folder contains classes `BasketSplitter` and `Utils` providing
implementation to resolve task.
Files inside `src/test/java/com/ocado/basket` contains unit tests of above classes.