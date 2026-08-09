package com.andreea.website_tech_scraper.runner;

import com.andreea.website_tech_scraper.dto.DomainResultDTO;
import com.andreea.website_tech_scraper.dto.TechRuleDTO;
import com.andreea.website_tech_scraper.services.ResourceLoaderService;
import com.andreea.website_tech_scraper.services.ScraperRunnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Executes the scraping process on application startup and exports results to JSON.
 */
@Component
public class ScraperRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ScraperRunner.class);

    private final ResourceLoaderService resourceLoaderService;
    private final ScraperRunnerService scraperRunnerService;

    public ScraperRunner(ResourceLoaderService resourceLoaderService, ScraperRunnerService scraperRunnerService) {
        this.resourceLoaderService = resourceLoaderService;
        this.scraperRunnerService = scraperRunnerService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading resources...");
        List<TechRuleDTO> rules = resourceLoaderService.loadRules("rules.json");
        List<String> domains = resourceLoaderService.loadDomains("domains.json");

        log.info("Starting analysis for {} domains using {} rules.", domains.size(), rules.size());
        long startTime = System.currentTimeMillis();

        List<DomainResultDTO> results = scraperRunnerService.processAllDomains(domains, rules);

        long endTime = System.currentTimeMillis();

        int totalTechnologiesFound = results.stream()
                .mapToInt(DomainResultDTO::getDetectedTechnologiesCount)
                .sum();

        log.info("Scan completed in {} seconds.", (endTime - startTime) / 1000);
        log.info("Total technologies detected across all domains: {}", totalTechnologiesFound);

        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(new File("results.json"), results);

        log.info("Results written to results.json");
    }
}