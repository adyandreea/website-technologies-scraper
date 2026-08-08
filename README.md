# Website Technologies Scraper

Website Technologies Scraper is an application for detecting web technologies on domains, built with Spring Boot and Jsoup

## How to run the app

1. **Run:**
   ```bash
   ./mvnw spring-boot:run

## Features
- Scans multiple domains at the same time using a thread pool.
- Matches technologies using a flexible JSON rules file (checks HTTP headers, meta tags, and scripts).
- Generates a `results.json` report.

## Tech Stack
- **Language:** Java 21
- **Framework:** Spring Boot
- **Parsing:** Jsoup
