package com.example.krishimitrabackend.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RateLimitingService {

    private final ProxyManager<String> proxyManager;

    @Value("${app.ratelimit.otp.capacity}")
    private long otpCapacity;

    @Value("${app.ratelimit.otp.refill-duration-seconds}")
    private long otpRefillDuration;

    private static final String keyPrefix = "ratelimit:otp:";

    public Bucket  bucketForOTp(String phoneNumber){
        Refill refill = Refill.greedy(otpCapacity, Duration.ofSeconds(otpRefillDuration));
        Bandwidth bandwidth = Bandwidth.classic(otpCapacity, refill);
        BucketConfiguration bucketConfiguration = BucketConfiguration.builder().addLimit(bandwidth).build();
        Supplier<BucketConfiguration> bucketSupplier = ()->bucketConfiguration;
        String redisKey = keyPrefix + phoneNumber;
        return proxyManager.builder().build(redisKey,bucketSupplier);
    }
}
