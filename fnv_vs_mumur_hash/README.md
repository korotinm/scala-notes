# Comparison FNV hash functions

## Configuration

- `MacBook Pro (Retina, 15-inch, Mid 2014)`
- `Processor 2,2 GHz Quad-Core Intel Core i7`
- `Memory 16 GB 1600 MHz DDR3`

## Command line instruction

- `jmh:run -i 3 -wi 4 -f1 -gc false`

---

## Here were considered 3 types of hash functions

- `FOV - 1` (32 bits)
- `FOV - 1a` (32 bits)
- `Murmur3` for contrast

---

## Here were called methods for hashing 100K - 2.1M words with length from 5 to 11 chars. Each word was created with using a random printable chars

| Benchmark | Mode | Cnt | Score | Error | Units |
--- | --- | --- | --- | --- | ---
HashBenchmarks.fnvMinus1_words_100_000 | thrpt | 6 | 104.707 | ± | 32.088 |ops/s
HashBenchmarks.fnvMinus1_words_400_000 | thrpt | 6 | 26.436 | ± | 1.300 |ops/s
HashBenchmarks.fnvMinus1_words_700_000 | thrpt | 6 | 15.047 | ± | 0.311 |ops/s
HashBenchmarks.fnvMinus1_words_1_000_000 | thrpt | 6 | 10.501 | ± | 0.462 |ops/s
HashBenchmarks.fnvMinus1_words_1_300_000 | thrpt | 6 | 8.128 | ± | 0.104 |ops/s
HashBenchmarks.fnvMinus1_words_1_600_000 | thrpt | 6 | 6.544 | ± | 0.199 |ops/s
HashBenchmarks.fnvMinus1_words_1_900_000 | thrpt | 6 | 5.542 | ± | 0.082 |ops/s
HashBenchmarks.fnvMinus1_words_2_100_000 | thrpt | 6 | 4.962 | ± | 0.196 |ops/s
HashBenchmarks.fnvMinus1a_words_100_000 | thrpt | 6 | 111.390 | ± | 4.007 |ops/s
HashBenchmarks.fnvMinus1a_words_400_000 | thrpt | 6 | 26.482 | ± | 1.407 |ops/s
HashBenchmarks.fnvMinus1a_words_700_000 | thrpt | 6 | 15.009 | ± | 0.658 |ops/s
HashBenchmarks.fnvMinus1a_words_1_000_000 | thrpt | 6 | 10.577 | ± | 0.163 |ops/s
HashBenchmarks.fnvMinus1a_words_1_300_000 | thrpt | 6 | 8.031 | ± | 0.253 |ops/s
HashBenchmarks.fnvMinus1a_words_1_600_000 | thrpt | 6 | 6.574 | ± | 0.109 |ops/s
HashBenchmarks.fnvMinus1a_words_1_900_000 | thrpt | 6 | 5.539 | ± | 0.088 |ops/s
HashBenchmarks.fnvMinus1a_words_2_100_000 | thrpt | 6 | 5.004 | ± | 0.040 |ops/s
HashBenchmarks.murmur3_words_100_000 | thrpt | 6 | 484.116 | ± | 13.840 |ops/s
HashBenchmarks.murmur3_words_400_000 | thrpt | 6 | 120.400 | ± | 4.159 |ops/s
HashBenchmarks.murmur3_words_700_000 | thrpt | 6 | 69.062 | ± | 3.507 |ops/s
HashBenchmarks.murmur3_words_1_000_000 | thrpt | 6 | 48.610 | ± | 0.518 |ops/s
HashBenchmarks.murmur3_words_1_300_000 | thrpt | 6 | 35.064 | ± | 2.797 |ops/s
HashBenchmarks.murmur3_words_1_600_000 | thrpt | 6 | 27.520 | ± | 0.810 |ops/s
HashBenchmarks.murmur3_words_1_900_000 | thrpt | 6 | 23.767 | ± | 0.638 |ops/s
HashBenchmarks.murmur3_words_2_100_000 | thrpt | 6 | 21.227 | ± | 1.176 |ops/s

## Result was disaggregated by count

| count | fnvMinus1 | fnvMinus1a | murmur3 |
--- | --- | --- | ---
100_000 | 104.707 | 111.390 | 484.116
400_000 | 26.436 | 26.482 | 120.400
700_000 | 15.047 | 15.009 | 69.062
1_000_000 | 10.501 | 10.577 | 48.610
1_300_000 | 8.128 | 8.031 | 35.064
1_600_000 | 6.544 | 6.574 | 27.520
1_900_000 | 5.542 | 5.539 | 23.767
2_100_000 | 4.962 | 5.004 | 21.227

## Conclusion

Hash algorithm `FNV - 1a` is optimised with applying arithmetics and binary operations in a different order.

In this algorithm binary operation is used first of all but in `FNV - 1` secondarily. It gives small advantages for `FNV - 1a` hash function.

#### Quote from [the document](https://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function#FNV-1a_hash):

**The change in order leads to slightly better [avalanche characteristics](https://en.wikipedia.org/wiki/Avalanche_effect).**
