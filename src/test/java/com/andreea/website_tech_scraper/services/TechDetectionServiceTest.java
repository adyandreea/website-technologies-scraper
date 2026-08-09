package com.andreea.website_tech_scraper.services;

import com.andreea.website_tech_scraper.dto.DomainResultDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TechDetectionServiceTest {

    @Test
    void testAnalyzeDomain() {
        TechDetectionService service = new TechDetectionService();

        DomainResultDTO result = service.analyzeDomain("invalid-domain-test-xyz-999.local", List.of());

        assertNotNull(result);
        assertEquals("FAILED", result.getStatus());
        assertEquals(0, result.getDetectedTechnologiesCount());
    }
}