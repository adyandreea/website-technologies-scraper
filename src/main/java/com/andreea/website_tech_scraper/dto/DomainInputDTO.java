package com.andreea.website_tech_scraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Domain input DTO.
 */
@Data
public class DomainInputDTO {
    @JsonProperty("root_domain")
    private String rootDomain;
}