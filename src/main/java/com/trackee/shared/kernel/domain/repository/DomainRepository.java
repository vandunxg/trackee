/* Copyright (c) 2026 Trackee */
package com.trackee.shared.kernel.domain.repository;

import java.util.List;
import java.util.Optional;

/**
 * @author vandunxg
 */
public interface DomainRepository<D, I> {
    Optional<D> findById(I id);

    List<D> findAllByIds(List<I> ids);

    D save(D domain);

    List<D> saveAll(List<D> domains);

    Optional<D> queryById(I id);
}
