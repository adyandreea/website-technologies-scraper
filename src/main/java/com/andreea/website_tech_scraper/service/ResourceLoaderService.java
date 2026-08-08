package com.andreea.website_tech_scraper.service;

import com.andreea.website_tech_scraper.dto.DomainInputDTO;
import com.andreea.website_tech_scraper.dto.TechRuleDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Resource loader service.
 */
@Service
public class ResourceLoaderService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<TechRuleDTO> loadRules(String rulesFileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(rulesFileName);
        try (InputStream is = resource.getInputStream()) {
            return objectMapper.readValue(is, new TypeReference<>() {
            });
        }
    }

    public List<String> loadDomains(String domainsFileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(domainsFileName);
        try (InputStream is = resource.getInputStream()) {
            List<DomainInputDTO> domainInputs = objectMapper.readValue(is, new TypeReference<>() {
            });

            return domainInputs.stream()
                    .map(DomainInputDTO::getRootDomain)
                    .collect(Collectors.toList());
        }
    }
}