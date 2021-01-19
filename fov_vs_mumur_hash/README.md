## This instruction was called for running all of tests below:
`jmh:run -i 3 -wi 4 -f1 -gc false`

---

## Here were considered 3 types of hash functions:
- `FOV - 1` (32 bits)
- `FOV - 1a` (32 bits)
- `Murmur3` for contrast

---

## Tests called method for hashing 100K - 2.1M words with length from 5 to 11 chars. Each word was created with using random number which converted to char type.

| Benchmark | Mode | Cnt | Score | Error | Units |
--- | --- | --- | --- | --- | ---
HashBenchmarks.fnvMinus1_words_100_000 | thrpt | 3 | 174.673 | ± 36.013 | ops/s
HashBenchmarks.fnvMinus1_words_400_000 | thrpt | 3 | 39.121 | ±  2.229 | ops/s
HashBenchmarks.fnvMinus1_words_700_000 | thrpt | 3 | 21.949 | ±  0.944 | ops/s
HashBenchmarks.fnvMinus1_words_1_000_000 | thrpt | 3 | 14.054 | ± 46.203 | ops/s
HashBenchmarks.fnvMinus1_words_1_300_000 | thrpt | 3 | 11.273 | ±  9.775 | ops/s
HashBenchmarks.fnvMinus1_words_1_600_000 | thrpt | 3 | 9.400 | ±  0.267 | ops/s
HashBenchmarks.fnvMinus1_words_1_900_000 | thrpt | 3 | 8.149 | ±  0.053 | ops/s
HashBenchmarks.fnvMinus1_words_2_100_000 | thrpt | 3 | 7.070 | ±  1.675 | ops/s
HashBenchmarks.fnvMinus1a_words_100_000 | thrpt | 3 | 124.321 | ± 46.754 | ops/s
HashBenchmarks.fnvMinus1a_words_400_000 | thrpt | 3 | 40.868 | ±  7.283 | ops/s
HashBenchmarks.fnvMinus1a_words_700_000 | thrpt | 3 | 22.073 | ±  0.094 | ops/s
HashBenchmarks.fnvMinus1a_words_1_000_000 | thrpt | 3 | 13.505 | ± 27.728 | ops/s
HashBenchmarks.fnvMinus1a_words_1_300_000 | thrpt | 3 | 11.931 | ±  1.268 | ops/s
HashBenchmarks.fnvMinus1a_words_1_600_000 | thrpt | 3 | 8.766 | ± 10.582 | ops/s
HashBenchmarks.fnvMinus1a_words_1_900_000 | thrpt | 3 | 8.064 | ±  0.712 | ops/s
HashBenchmarks.fnvMinus1a_words_2_100_000 | thrpt | 3 | 7.127 | ±  0.229 | ops/s
HashBenchmarks.murmur3_words_100_000 | thrpt | 3 | 420.400 | ± 47.349 | ops/s
HashBenchmarks.murmur3_words_400_000 | thrpt | 3 | 75.117 | ±  4.075 | ops/s
HashBenchmarks.murmur3_words_700_000 | thrpt | 3 | 54.539 | ±  5.473 | ops/s
HashBenchmarks.murmur3_words_1_000_000 | thrpt | 3 | 28.389 | ± 45.412 | ops/s
HashBenchmarks.murmur3_words_1_300_000 | thrpt | 3 | 28.122 | ±  5.071 | ops/s
HashBenchmarks.murmur3_words_1_600_000 | thrpt | 3 | 16.075 | ± 11.694 | ops/s
HashBenchmarks.murmur3_words_1_900_000 | thrpt | 3 | 17.662 | ±  2.213 | ops/s
HashBenchmarks.murmur3_words_2_100_000 | thrpt | 3 | 13.780 | ±  0.956 | ops/s

---

## Tests called method for hashing 100K - 2.1M words with length from 5 to 11 chars. Each word was created with using random printable chars.

| Benchmark | Mode | Cnt | Score | Error | Units |
--- | --- | --- | --- | --- | ---
WithPrintableCharsHashBenchmarks.fnvMinus1_words_100_000 | thrpt | 3 | 116.859 | ±  7.821 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1_words_400_000 | thrpt | 3 | 22.781 | ± 31.751 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1_words_700_000 | thrpt | 3 | 12.091 | ±  8.342 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1_words_1_000_000 | thrpt | 3 | 8.678 | ±  0.953 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1_words_1_300_000 | thrpt | 3 | 8.517 | ±  0.615 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1_words_1_600_000 | thrpt | 3 | 6.597 | ±  0.268 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1_words_1_900_000 | thrpt | 3 | 4.997 | ± 13.451 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1_words_2_100_000 | thrpt | 3 | 3.936 | ±  2.315 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1a_words_100_000 | thrpt | 3 | 93.467 | ± 84.484 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1a_words_400_000 | thrpt | 3 | 28.066 | ±  2.498 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1a_words_700_000 | thrpt | 3 | 15.734 | ±  1.615 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1a_words_1_000_000 | thrpt | 3 | 10.905 | ±  0.416 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1a_words_1_300_000 | thrpt | 3 | 8.318 | ±  1.348 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1a_words_1_600_000 | thrpt | 3 | 6.729 | ±  1.563 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1a_words_1_900_000 | thrpt | 3 | 5.475 | ±  9.738 | ops/s
WithPrintableCharsHashBenchmarks.fnvMinus1a_words_2_100_000 | thrpt | 3 | 5.227 | ±  0.642 | ops/s
WithPrintableCharsHashBenchmarks.murmur3_words_100_000 | thrpt | 3 | 482.276 | ± 21.645 | ops/s
WithPrintableCharsHashBenchmarks.murmur3_words_400_000 | thrpt | 3 | 70.653 | ± 59.031 | ops/s
WithPrintableCharsHashBenchmarks.murmur3_words_700_000 | thrpt | 3 | 58.652 | ±  0.989 | ops/s
WithPrintableCharsHashBenchmarks.murmur3_words_1_000_000 | thrpt | 3 | 38.093 | ±  0.643 | ops/s
WithPrintableCharsHashBenchmarks.murmur3_words_1_300_000 | thrpt | 3 | 17.583 | ± 25.266 | ops/s
WithPrintableCharsHashBenchmarks.murmur3_words_1_600_000 | thrpt | 3 | 17.226 | ±  9.790 | ops/s
WithPrintableCharsHashBenchmarks.murmur3_words_1_900_000 | thrpt | 3 | 19.743 | ±  0.639 | ops/s
WithPrintableCharsHashBenchmarks.murmur3_words_2_100_000 | thrpt | 3 | 14.430 | ± 18.133 | ops/s