-- Initial Development Seed Records (data.sql)
-- Five realistic research papers representing different review lifecycle states

INSERT INTO papers (
    title, authors, abstract_text, keywords, publication_year, doi_url, 
    submitter_email, submitter_username, review_status, created_at, updated_at
) VALUES (
    'Automated Continuous Integration Pipelines in Cloud Architectures',
    'Ada Lovelace, Charles Babbage',
    'This paper examines the performance implications of automated build pipelines, containerized build agents, and automated testing frameworks in modern enterprise cloud architectures.',
    'DevOps, CI/CD, Jenkins, Automation',
    2024,
    'https://doi.org/10.1000/182',
    'ada@analytic-engine.org',
    'researcher',
    'ACCEPTED',
    TIMESTAMP '2026-01-10 09:30:00',
    TIMESTAMP '2026-01-15 14:20:00'
);

INSERT INTO papers (
    title, authors, abstract_text, keywords, publication_year, doi_url, 
    submitter_email, submitter_username, review_status, created_at, updated_at
) VALUES (
    'Empirical Analysis of In-Memory Databases in Academic Repositories',
    'Alan Turing',
    'We present a systematic benchmark comparing volatile in-memory storage against persistent relational databases for micro-workloads in educational repository software.',
    'Databases, H2, In-Memory, Performance',
    2025,
    'https://doi.org/10.1000/183',
    'alan@turing-institute.org',
    'researcher',
    'UNDER_REVIEW',
    TIMESTAMP '2026-02-01 10:15:00',
    TIMESTAMP '2026-02-02 11:00:00'
);

INSERT INTO papers (
    title, authors, abstract_text, keywords, publication_year, doi_url, 
    submitter_email, submitter_username, review_status, created_at, updated_at
) VALUES (
    'Security Vulnerabilities in Automated Webhook Ingestion Mechanisms',
    'Grace Hopper',
    'An investigation into replay attacks, unauthenticated payloads, and injection risks in continuous deployment webhooks, proposing an HMAC signature verification standard.',
    'Security, Webhooks, CI/CD, DevSecOps',
    2025,
    'https://doi.org/10.1000/184',
    'grace@compiler-pioneers.gov',
    'researcher',
    'SUBMITTED',
    TIMESTAMP '2026-02-15 08:45:00',
    TIMESTAMP '2026-02-15 08:45:00'
);

INSERT INTO papers (
    title, authors, abstract_text, keywords, publication_year, doi_url, 
    submitter_email, submitter_username, review_status, created_at, updated_at
) VALUES (
    'Microservice Decomposition Strategies for Monolithic Academic Repositories',
    'Margaret Hamilton',
    'A retrospective study of migrating legacy monoliths to loosely coupled asynchronous microservices, highlighting operational overhead and failure domain isolation.',
    'Microservices, Architecture, Distributed Systems',
    2023,
    'https://doi.org/10.1000/185',
    'margaret@apollo-systems.edu',
    'researcher',
    'REVISION_REQUESTED',
    TIMESTAMP '2026-01-20 16:00:00',
    TIMESTAMP '2026-01-28 12:30:00'
);

INSERT INTO papers (
    title, authors, abstract_text, keywords, publication_year, doi_url, 
    submitter_email, submitter_username, review_status, created_at, updated_at
) VALUES (
    'Predictive Keyword Indexing Using Primitive Markov Models',
    'Claude Shannon',
    'Early exploratory work on statistical frequency distribution of keyword tokens in document metadata repositories. Rejected due to insufficient validation against modern neural indexing.',
    'Information Theory, Indexing, Text Processing',
    2022,
    'https://doi.org/10.1000/186',
    'claude@bell-labs.com',
    'researcher',
    'REJECTED',
    TIMESTAMP '2025-11-05 13:00:00',
    TIMESTAMP '2025-11-18 09:15:00'
);
