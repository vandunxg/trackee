/* Copyright (c) 2026 Trackee */
package com.trackee.infrastructure.common.persitence;

import com.trackee.shared.infrastructure.mapper.PersistenceMapper;
import com.trackee.shared.kernel.domain.repository.DomainRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author vandunxg
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractDomainRepository<D, E, I> implements DomainRepository<D, I> {

    JpaRepository<E, I> jpaRepository;
    PersistenceMapper<D, E> mapper;

    protected AbstractDomainRepository(JpaRepository<E, I> jpaRepository, PersistenceMapper<D, E> mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<D> findById(I id) {
        Optional<E> var10000 = this.jpaRepository.findById(id);
        PersistenceMapper<D, E> var10001 = this.mapper;
        Objects.requireNonNull(var10001);
        return var10000.map(var10001::toDomain).map(this::enrich);
    }

    @Override
    public Optional<D> queryById(I id) {
        Optional<E> var10000 = this.jpaRepository.findById(id);
        PersistenceMapper<D, E> var10001 = this.mapper;
        Objects.requireNonNull(var10001);
        return var10000.map(var10001::toDomain);
    }

    @Override
    public List<D> findAllByIds(List<I> ids) {
        Stream<E> var10001 = this.jpaRepository.findAllById(ids).stream();
        PersistenceMapper<D, E> var10002 = this.mapper;
        Objects.requireNonNull(var10002);
        return this.enrichList(var10001.map(var10002::toDomain).collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public D save(D domain) {
        this.saveAll(List.of(domain));
        return domain;
    }

    @Transactional
    @Override
    public List<D> saveAll(List<D> domains) {
        List<E> entities = this.mapper.toEntity(domains);
        this.jpaRepository.saveAll(entities);
        return domains;
    }

    D enrich(D d) {
        List<D> ds = List.of(d);
        return this.enrichList(ds).getFirst();
    }

    List<D> enrichList(List<D> ds) {
        return ds;
    }
}
