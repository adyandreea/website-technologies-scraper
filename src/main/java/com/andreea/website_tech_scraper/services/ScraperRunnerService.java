package com.andreea.website_tech_scraper.services;

import com.andreea.website_tech_scraper.dto.DomainResultDTO;
import com.andreea.website_tech_scraper.dto.TechRuleDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Runs domain analysis concurrently across multiple threads.
 */
@Service
public class ScraperRunnerService {

    private final TechDetectionService detectionService;
    private final ExecutorService executor = Executors.newFixedThreadPool(20);

    public ScraperRunnerService(TechDetectionService detectionService) {
        this.detectionService = detectionService;
    }

    public List<DomainResultDTO> processAllDomains(List<String> domains, List<TechRuleDTO> rules) {
        List<CompletableFuture<DomainResultDTO>> futures = domains.stream()
                .map(domain -> CompletableFuture.supplyAsync(() -> detectionService.analyzeDomain(domain, rules), executor))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}