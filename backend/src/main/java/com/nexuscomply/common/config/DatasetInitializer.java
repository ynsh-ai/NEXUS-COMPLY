package com.nexuscomply.common.config;

import com.nexuscomply.framework.service.DatasetImportService;
import com.nexuscomply.framework.service.DatasetValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

@Component
public class DatasetInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatasetInitializer.class);

    private final DatasetImportService datasetImportService;
    private final MongoTemplate mongoTemplate;

    public DatasetInitializer(DatasetImportService datasetImportService,
                              MongoTemplate mongoTemplate) {
        this.datasetImportService = datasetImportService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            ensureRuntimeCollectionIndexes();
            DatasetValidationResult result = datasetImportService.validateAndImport();
            log.info("NEXUS-COMPLY reference dataset initialization complete: valid={}, inserted/updated={}, errors={}",
                    result.isValid(), result.getInsertedCount(), result.getErrors().size());
        } catch (Exception e) {
            log.error("Error during reference dataset initialization: {}", e.getMessage(), e);
        }
    }

    private void ensureRuntimeCollectionIndexes() {
        try {
            mongoTemplate.indexOps("audits")
                    .ensureIndex(new Index().on("startedAt", Sort.Direction.DESC));
            mongoTemplate.indexOps("findings")
                    .ensureIndex(new Index().on("auditId", Sort.Direction.ASC).on("deviceId", Sort.Direction.ASC));
            mongoTemplate.indexOps("evidence")
                    .ensureIndex(new Index().on("findingId", Sort.Direction.ASC));
            mongoTemplate.indexOps("normalized_configurations")
                    .ensureIndex(new Index().on("configurationId", Sort.Direction.ASC).on("versionId", Sort.Direction.ASC));
            mongoTemplate.indexOps("what_if_simulations")
                    .ensureIndex(new Index().on("createdAt", Sort.Direction.DESC));
            mongoTemplate.indexOps("drift_events")
                    .ensureIndex(new Index().on("deviceId", Sort.Direction.ASC).on("detectedAt", Sort.Direction.DESC));
            log.info("MongoDB runtime collection indexes ensured.");
        } catch (Exception e) {
            log.warn("Notice when ensuring runtime collection indexes: {}", e.getMessage());
        }
    }
}
