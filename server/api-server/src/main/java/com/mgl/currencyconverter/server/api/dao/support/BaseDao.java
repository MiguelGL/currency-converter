package com.mgl.currencyconverter.server.api.dao.support;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mgl.currencyconverter.server.api.entity.support.BaseEntity;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseDao<E extends BaseEntity, P extends EntityPathBase<E>> {

    protected @PersistenceContext EntityManager em;

    protected final @NonNull Class<E> clazz;
    protected final @NonNull P pathBase;

    protected JPAQuery<E> queryFromMe() {
        return new JPAQuery<E>(em).from(pathBase);
    }

    protected JPADeleteClause deleteFromMe() {
        return new JPADeleteClause(em, pathBase);
    }

    public List<E> findAll() {
        return queryFromMe().fetch();
    }

    public void create(E entity) {
        em.persist(entity);
    }

    public E update(E entity) {
        return em.merge(entity);
    }

}
