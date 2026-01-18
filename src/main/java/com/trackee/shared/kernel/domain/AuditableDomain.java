/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

/**
 * @author vandunxg
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class AuditableDomain {

    String createdBy;
    Instant createdAt;
    String lastModifiedBy;
    Instant lastModifiedAt;

    protected AuditableDomain(final AuditableDomainBuilder<?, ?> b) {
        this.createdBy = b.createdBy;
        this.createdAt = b.createdAt;
        this.lastModifiedBy = b.lastModifiedBy;
        this.lastModifiedAt = b.lastModifiedAt;
    }

    public abstract static class AuditableDomainBuilder<
            C extends AuditableDomain, B extends AuditableDomainBuilder<C, B>> {
        private String createdBy;
        private Instant createdAt;
        private String lastModifiedBy;
        private Instant lastModifiedAt;

        public B createdBy(final String createdBy) {
            this.createdBy = createdBy;
            return (B) this.self();
        }

        public B createdAt(final Instant createdAt) {
            this.createdAt = createdAt;
            return (B) this.self();
        }

        public B lastModifiedBy(final String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
            return (B) this.self();
        }

        public B lastModifiedAt(final Instant lastModifiedAt) {
            this.lastModifiedAt = lastModifiedAt;
            return (B) this.self();
        }

        protected abstract B self();

        public abstract C build();

        @Override
        public String toString() {
            return "AuditableDomain.AuditableDomainBuilder(createdBy="
                    + this.createdBy
                    + ", createdAt="
                    + this.createdAt
                    + ", lastModifiedBy="
                    + this.lastModifiedBy
                    + ", lastModifiedAt="
                    + this.lastModifiedAt
                    + ")";
        }
    }

    private static final class AuditableDomainBuilderImpl
            extends AuditableDomainBuilder<AuditableDomain, AuditableDomainBuilderImpl> {

        @Override
        protected AuditableDomainBuilderImpl self() {
            return this;
        }

        @Override
        public AuditableDomain build() {
            return new AuditableDomain(this);
        }
    }
}
