/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.iam.persistence;

import com.trackee.infrastructure.iam.persistence.entity.OtpCodeEntity;
import com.trackee.shared.kernel.domain.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author vandunxg
 */
public interface OtpCodeJpaRepository extends JpaRepository<OtpCodeEntity, UUID> {

    @Query(
            """
        from OtpCodeEntity o
        where o.deletedAt is null and o.userId = :userId and o.otpPurpose = :purpose
        order by o.createdAt desc
        limit 1
        """)
    Optional<OtpCodeEntity> findLatestOtpCodeByUserId(
            @Param("userId") UUID userId, @Param("purpose") OtpPurpose purpose);

    @Query(
            """
        from OtpCodeEntity o
        where o.deletedAt is null
            and o.userId = :userId
            and o.otpPurpose = :purpose
            and o.usedAt is null
            and o.revokedAt is null
        """)
    List<OtpCodeEntity> findAllOtpCodesNotUsedByUserId(UUID userId, OtpPurpose purpose);

    @Query(
            """
                        from OtpCodeEntity o
                        where o.deletedAt is null
                            and o.hashedCode = :hashedCode
                    """)
    Optional<OtpCodeEntity> findByHashedCode(String hashedCode);
}
