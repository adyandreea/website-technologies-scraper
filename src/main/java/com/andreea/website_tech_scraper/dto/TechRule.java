package com.andreea.website_tech_scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechRule {
    private String name;
    private String category;
    private Map<String, String> headers;
    private String metaGenerator;
    private String scriptSrc;
    private String htmlBody;
}
