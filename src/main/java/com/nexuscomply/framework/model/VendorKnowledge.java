package com.nexuscomply.framework.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "vendor_knowledge")
public class VendorKnowledge {
    @Id
    @JsonProperty("id")
    @JsonAlias({"id", "_id"})
    private String id;

    private String vendor;
    private String platform;

    @JsonProperty("raw_syntax_pattern")
    @JsonAlias({"raw_syntax_pattern", "rawSyntaxPattern"})
    private String rawSyntaxPattern;

    @JsonProperty("canonical_field")
    @JsonAlias({"canonical_field", "canonicalField"})
    private String canonicalField;

    @JsonProperty("canonical_value")
    @JsonAlias({"canonical_value", "canonicalValue"})
    private String canonicalValue;

    @JsonProperty("source_note")
    @JsonAlias({"source_note", "sourceNote"})
    private String sourceNote;

    @JsonProperty("source_version")
    @JsonAlias({"source_version", "sourceVersion"})
    private String sourceVersion;

    @JsonProperty("source_reference")
    @JsonAlias({"source_reference", "sourceReference"})
    private String sourceReference;

    public VendorKnowledge() {}

    public VendorKnowledge(String id, String vendor, String platform, String rawSyntaxPattern, String canonicalField, String canonicalValue, String sourceNote) {
        this.id = id;
        this.vendor = vendor;
        this.platform = platform;
        this.rawSyntaxPattern = rawSyntaxPattern;
        this.canonicalField = canonicalField;
        this.canonicalValue = canonicalValue;
        this.sourceNote = sourceNote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getRawSyntaxPattern() {
        return rawSyntaxPattern;
    }

    public void setRawSyntaxPattern(String rawSyntaxPattern) {
        this.rawSyntaxPattern = rawSyntaxPattern;
    }

    public String getCanonicalField() {
        return canonicalField;
    }

    public void setCanonicalField(String canonicalField) {
        this.canonicalField = canonicalField;
    }

    public String getCanonicalValue() {
        return canonicalValue;
    }

    public void setCanonicalValue(String canonicalValue) {
        this.canonicalValue = canonicalValue;
    }

    public String getSourceNote() {
        return sourceNote;
    }

    public void setSourceNote(String sourceNote) {
        this.sourceNote = sourceNote;
    }

    public String getSourceVersion() {
        return sourceVersion;
    }

    public void setSourceVersion(String sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }
}
