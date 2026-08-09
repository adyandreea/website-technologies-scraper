package com.andreea.website_tech_scraper.services;

import com.andreea.website_tech_scraper.dto.TechRuleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResourceLoaderServiceTest {

    private ResourceLoaderService loaderService;

    @BeforeEach
    void setUp() {
        loaderService = new ResourceLoaderService();
    }

    @Test
    void testLoadRulesSuccess() throws IOException {
        List<TechRuleDTO> rules = loaderService.loadRules("rules.json");

        assertNotNull(rules);
        assertFalse(rules.isEmpty());
        assertEquals("Shopify", rules.getFirst().getName());
        assertEquals("Ecommerce", rules.getFirst().getCategory());
    }

    @Test
    void testLoadRulesFileNotFound() {
        assertThrows(IOException.class, () -> {
            loaderService.loadRules("non_existent_file.json");
        });
    }

    @Test
    void testLoadDomainsSuccess() throws IOException {
        List<String> domains = loaderService.loadDomains("domains.json");

        assertNotNull(domains);
        assertFalse(domains.isEmpty());
    }

    @Test
    void testLoadDomainsFileNotFound() {
        assertThrows(IOException.class, () -> {
            loaderService.loadDomains("non_existent_file.json");
        });
    }
}