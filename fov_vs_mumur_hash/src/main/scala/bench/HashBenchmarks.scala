package bench

import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Group
import org.openjdk.jmh.annotations.GroupThreads
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.OperationsPerInvocation
import scala.util.Random
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.Level
import scala.util.hashing.MurmurHash3

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Array(Mode.Throughput))
class HashBenchmarks {

  @Benchmark
  def fnvMinus1a_words_100_000(wordState: WordState_100000) =
    wordState.words.foreach(FOVHash.fnvMinus1a(_))

  @Benchmark
  def fnvMinus1a_words_400_000(wordState: WordState_400000) =
    wordState.words.foreach(FOVHash.fnvMinus1a(_))

  @Benchmark
  def fnvMinus1a_words_700_000(wordState: WordState_700000) =
    wordState.words.foreach(FOVHash.fnvMinus1a(_))

  @Benchmark
  def fnvMinus1a_words_1_000_000(wordState: WordState_1000000) =
    wordState.words.foreach(FOVHash.fnvMinus1a(_))

  @Benchmark
  def fnvMinus1a_words_1_300_000(wordState: WordState_1300000) =
    wordState.words.foreach(FOVHash.fnvMinus1a(_))

  @Benchmark
  def fnvMinus1a_words_1_600_000(wordState: WordState_1600000) =
    wordState.words.foreach(FOVHash.fnvMinus1a(_))

  @Benchmark
  def fnvMinus1a_words_1_900_000(wordState: WordState_1900000) =
    wordState.words.foreach(FOVHash.fnvMinus1a(_))

  @Benchmark
  def fnvMinus1a_words_2_100_000(wordState: WordState_2100000) =
    wordState.words.foreach(FOVHash.fnvMinus1a(_))

  @Benchmark
  def fnvMinus1_words_100_000(wordState: WordState_100000) =
    wordState.words.foreach(FOVHash.fnvMinus1(_))

  @Benchmark
  def fnvMinus1_words_400_000(wordState: WordState_400000) =
    wordState.words.foreach(FOVHash.fnvMinus1(_))

  @Benchmark
  def fnvMinus1_words_700_000(wordState: WordState_700000) =
    wordState.words.foreach(FOVHash.fnvMinus1(_))

  @Benchmark
  def fnvMinus1_words_1_000_000(wordState: WordState_1000000) =
    wordState.words.foreach(FOVHash.fnvMinus1(_))

  @Benchmark
  def fnvMinus1_words_1_300_000(wordState: WordState_1300000) =
    wordState.words.foreach(FOVHash.fnvMinus1(_))

  @Benchmark
  def fnvMinus1_words_1_600_000(wordState: WordState_1600000) =
    wordState.words.foreach(FOVHash.fnvMinus1(_))

  @Benchmark
  def fnvMinus1_words_1_900_000(wordState: WordState_1900000) =
    wordState.words.foreach(FOVHash.fnvMinus1(_))

  @Benchmark
  def fnvMinus1_words_2_100_000(wordState: WordState_2100000) =
    wordState.words.foreach(FOVHash.fnvMinus1(_))

  @Benchmark
  def murmur3_words_100_000(wordState: WordState_100000) =
    wordState.words.foreach(MurmurHash3.stringHash(_))

  @Benchmark
  def murmur3_words_400_000(wordState: WordState_400000) =
    wordState.words.foreach(MurmurHash3.stringHash(_))

  @Benchmark
  def murmur3_words_700_000(wordState: WordState_700000) =
    wordState.words.foreach(MurmurHash3.stringHash(_))

  @Benchmark
  def murmur3_words_1_000_000(wordState: WordState_1000000) =
    wordState.words.foreach(MurmurHash3.stringHash(_))

  @Benchmark
  def murmur3_words_1_300_000(wordState: WordState_1300000) =
    wordState.words.foreach(MurmurHash3.stringHash(_))

  @Benchmark
  def murmur3_words_1_600_000(wordState: WordState_1600000) =
    wordState.words.foreach(MurmurHash3.stringHash(_))

  @Benchmark
  def murmur3_words_1_900_000(wordState: WordState_1900000) =
    wordState.words.foreach(MurmurHash3.stringHash(_))

  @Benchmark
  def murmur3_words_2_100_000(wordState: WordState_2100000) =
    wordState.words.foreach(MurmurHash3.stringHash(_))
}
