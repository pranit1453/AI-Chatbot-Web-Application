package com.demo.ai.schedular;

import com.demo.ai.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {

    private final UserJpaRepository userJpaRepository;

    // Runs automatically every day at 3 AM
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupDeletedUsers() {
        Instant cutoff = Instant.now().minus(7, ChronoUnit.DAYS);
        userJpaRepository.deleteUsersPermanently(cutoff);
    }
}
