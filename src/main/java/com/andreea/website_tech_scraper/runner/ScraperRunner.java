package com.andreea.website_tech_scraper.runner;

import com.andreea.website_tech_scraper.dto.DomainResultDTO;
import com.andreea.website_tech_scraper.dto.TechRuleDTO;
import com.andreea.website_tech_scraper.service.ResourceLoaderService;
import com.andreea.website_tech_scraper.service.ScraperRunnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Executes the scraping process on application startup and exports results to JSON.
 */
@Component
public class ScraperRunner implements CommandLineRunner {

    private final ResourceLoaderService resourceLoaderService;
    private final ScraperRunnerService scraperRunnerService;

    public ScraperRunner(ResourceLoaderService resourceLoaderService, ScraperRunnerService scraperRunnerService) {
        this.resourceLoaderService = resourceLoaderService;
        this.scraperRunnerService = scraperRunnerService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Loading resources...");
        List<TechRuleDTO> rules = resourceLoaderService.loadRules("rules.json");
        List<String> domains = resourceLoaderService.loadDomains("domains.json");

        System.out.println("Starting analysis for " + domains.size() + " domains using " + rules.size() + " rules.");
        long startTime = System.currentTimeMillis();

        List<DomainResultDTO> results = scraperRunnerService.processAllDomains(domains, rules);

        long endTime = System.currentTimeMillis();
        System.out.println("Scan completed in " + (endTime - startTime) / 1000 + " seconds.");

        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(new File("results.json"), results);

        System.out.println("Results written to results.json");
    }
}