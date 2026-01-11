/* Copyright (c) 2026 Trackee */
package com.trackee.shared.infrastructure.mapper;

import java.util.List;

/**
 * @author vandunxg
 */
public interface PersistenceMapper<D, E> {

    D toDomain(E entity);

    List<D> toDomain(List<E> entities);

    E toEntity(D domain);

    List<E> toEntity(List<D> domains);
}
