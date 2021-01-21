# Comparison FOV hash functions

## Configuration
- `MacBook Pro (Retina, 15-inch, Mid 2014)`
- `Processor 2,2 GHz Quad-Core Intel Core i7`
- `Memory 16 GB 1600 MHz DDR3`

## This instruction was called for running all of tests below

- `jmh:run -i 3 -wi 4 -f1 -gc false`

---

## Here were considered 3 types of hash functions

- `FOV - 1` (32 bits)
- `FOV - 1a` (32 bits)
- `Murmur3` for contrast

---

## Were called methods for hashing 100K - 2.1M words with length from 5 to 11 chars. Each word was created with using random printable chars

| Benchmark | Mode | Cnt | Score | Error | Units |
--- | --- | --- | --- | --- | ---
HashBenchmarks.fnvMinus1_words_100_000 | thrpt | 3 | 116.859 | ±  7.821 | ops/s
HashBenchmarks.fnvMinus1_words_400_000 | thrpt | 3 | 22.781 | ± 31.751 | ops/s
HashBenchmarks.fnvMinus1_words_700_000 | thrpt | 3 | 12.091 | ±  8.342 | ops/s
HashBenchmarks.fnvMinus1_words_1_000_000 | thrpt | 3 | 8.678 | ±  0.953 | ops/s
HashBenchmarks.fnvMinus1_words_1_300_000 | thrpt | 3 | 8.517 | ±  0.615 | ops/s
HashBenchmarks.fnvMinus1_words_1_600_000 | thrpt | 3 | 6.597 | ±  0.268 | ops/s
HashBenchmarks.fnvMinus1_words_1_900_000 | thrpt | 3 | 4.997 | ± 13.451 | ops/s
HashBenchmarks.fnvMinus1_words_2_100_000 | thrpt | 3 | 3.936 | ±  2.315 | ops/s
HashBenchmarks.fnvMinus1a_words_100_000 | thrpt | 3 | 93.467 | ± 84.484 | ops/s
HashBenchmarks.fnvMinus1a_words_400_000 | thrpt | 3 | 28.066 | ±  2.498 | ops/s
HashBenchmarks.fnvMinus1a_words_700_000 | thrpt | 3 | 15.734 | ±  1.615 | ops/s
HashBenchmarks.fnvMinus1a_words_1_000_000 | thrpt | 3 | 10.905 | ±  0.416 | ops/s
HashBenchmarks.fnvMinus1a_words_1_300_000 | thrpt | 3 | 8.318 | ±  1.348 | ops/s
HashBenchmarks.fnvMinus1a_words_1_600_000 | thrpt | 3 | 6.729 | ±  1.563 | ops/s
HashBenchmarks.fnvMinus1a_words_1_900_000 | thrpt | 3 | 5.475 | ±  9.738 | ops/s
HashBenchmarks.fnvMinus1a_words_2_100_000 | thrpt | 3 | 5.227 | ±  0.642 | ops/s
HashBenchmarks.murmur3_words_100_000 | thrpt | 3 | 482.276 | ± 21.645 | ops/s
HashBenchmarks.murmur3_words_400_000 | thrpt | 3 | 70.653 | ± 59.031 | ops/s
HashBenchmarks.murmur3_words_700_000 | thrpt | 3 | 58.652 | ±  0.989 | ops/s
HashBenchmarks.murmur3_words_1_000_000 | thrpt | 3 | 38.093 | ±  0.643 | ops/s
HashBenchmarks.murmur3_words_1_300_000 | thrpt | 3 | 17.583 | ± 25.266 | ops/s
HashBenchmarks.murmur3_words_1_600_000 | thrpt | 3 | 17.226 | ±  9.790 | ops/s
HashBenchmarks.murmur3_words_1_900_000 | thrpt | 3 | 19.743 | ±  0.639 | ops/s
HashBenchmarks.murmur3_words_2_100_000 | thrpt | 3 | 14.430 | ± 18.133 | ops/s
