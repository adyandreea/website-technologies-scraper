package com.andreea.website_tech_scraper.services;

import com.andreea.website_tech_scraper.dto.DomainResultDTO;
import com.andreea.website_tech_scraper.dto.TechRuleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ScraperRunnerServiceTest {

    private TechDetectionService detectionServiceMock;
    private ScraperRunnerService scraperRunnerService;

    @BeforeEach
    void setUp() {
        detectionServiceMock = Mockito.mock(TechDetectionService.class);
        scraperRunnerService = new ScraperRunnerService(detectionServiceMock);
    }

    @Test
    void testProcessAllDomainsSuccess() {
        List<String> domains = List.of("example.com", "test.com");
        List<TechRuleDTO> rules = Collections.emptyList();

        DomainResultDTO mockResult1 = DomainResultDTO.builder().domain("example.com").status("SUCCESS").build();
        DomainResultDTO mockResult2 = DomainResultDTO.builder().domain("test.com").status("SUCCESS").build();

        when(detectionServiceMock.analyzeDomain(eq("example.com"), eq(rules))).thenReturn(mockResult1);
        when(detectionServiceMock.analyzeDomain(eq("test.com"), eq(rules))).thenReturn(mockResult2);

        List<DomainResultDTO> results = scraperRunnerService.processAllDomains(domains, rules);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("example.com", results.get(0).getDomain());
        assertEquals("test.com", results.get(1).getDomain());
    }

    @Test
    void testProcessAllDomainsEmptyList() {
        List<String> domains = Collections.emptyList();
        List<TechRuleDTO> rules = Collections.emptyList();

        List<DomainResultDTO> results = scraperRunnerService.processAllDomains(domains, rules);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}