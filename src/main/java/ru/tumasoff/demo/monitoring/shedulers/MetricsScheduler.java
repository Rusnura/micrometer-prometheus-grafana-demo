package ru.tumasoff.demo.monitoring.shedulers;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MetricsScheduler {
  private static final Random random = new Random();
  private final AtomicInteger testGauge;
  private final Counter testCounter;

  public MetricsScheduler(MeterRegistry meterRegistry) {
    // Counter vs. gauge, summary vs. histogram
    // https://prometheus.io/docs/practices/instrumentation/#counter-vs-gauge-summary-vs-histogram
    testGauge = meterRegistry.gauge("custom_gauge", new AtomicInteger(0));
    testCounter = meterRegistry.counter("custom_counter");
  }

  @Scheduled(fixedRateString = "1000", initialDelayString = "0")
  public void schedulingTask() {
    testGauge.set(MetricsScheduler.getRandomNumberInRange(0, 100));
    testCounter.increment();
  }

  private static int getRandomNumberInRange(int min, int max) {
    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }
    return random.nextInt((max - min) + 1) + min;
  }
}
