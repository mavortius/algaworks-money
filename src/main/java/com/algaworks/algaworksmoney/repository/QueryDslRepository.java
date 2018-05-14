package com.algaworks.algaworksmoney.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * QueryDsl specific class for implementing repositories using QueryDsl library which adds implementation for
 * {@link QueryDslPredicateExecutor}.
 *
 * @author Marcelo Martins
 */
public abstract class QueryDslRepository<T, ID extends Serializable>
        extends QueryDslRepositorySupport implements QueryDslPredicateExecutor<T> {

    private final EntityPath<T> entityPath;

    /**
     * Creates a new {@link QueryDslRepository} instance for the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     */
    public QueryDslRepository(Class<T> domainClass) {
        super(domainClass);

        EntityManager em = getEntityManager();
        this.entityPath = getBuilder();
    }

    /**
     * @see {@link QueryDslPredicateExecutor#findOne(Predicate)}
     */
    @Override
    public T findOne(Predicate predicate) {
        return createQuery(predicate).fetchFirst();
    }

    /**
     * @see {@link QueryDslPredicateExecutor#findAll(Predicate)}
     */
    @Override
    public Iterable<T> findAll(Predicate predicate) {
        return createQuery(predicate).fetch();
    }

    /**
     * @see {@link QueryDslPredicateExecutor#findAll(Predicate, Sort)}
     */
    @Override
    public Iterable<T> findAll(Predicate predicate, Sort sort) {
        return executeSorted(createQuery(predicate).select(entityPath), sort);
    }

    /**
     * @see {@link QueryDslPredicateExecutor#findAll(Predicate, OrderSpecifier[])}
     */
    @Override
    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        return createQuery(predicate).orderBy(orders).fetch();
    }

    /**
     * @see {@link QueryDslPredicateExecutor#findAll(OrderSpecifier[])}
     */
    @Override
    public Iterable<T> findAll(OrderSpecifier<?>... orders) {
        return createQuery().orderBy(orders).fetch();
    }

    /**
     * @see {@link QueryDslPredicateExecutor#findAll(Predicate, Pageable)}
     */
    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        final JPQLQuery<?> countQuery = createQuery(predicate);
        JPQLQuery<T> query = getQuerydsl().applyPagination(pageable, createQuery(predicate).select(entityPath));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, () -> countQuery.fetchCount());
    }

    public <P> Page<?> findAll(Predicate predicate, Pageable pageable, Expression<P> expression) {
        final JPQLQuery<?> countQuery = createQuery(predicate);
        JPQLQuery<?> query = getQuerydsl().applyPagination(pageable, createQuery(predicate).select(expression));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, () -> countQuery.fetchCount());
    }

    public <P> List<?> findAll(Predicate predicate, Expression<?> groupBy, Expression<P> expression) {
        return createQuery(predicate).select(expression).groupBy(groupBy).fetch();
    }

    /**
     * @see {@link QueryDslPredicateExecutor#count(Predicate)}
     */
    @Override
    public long count(Predicate predicate) {
        return createQuery(predicate).fetchCount();
    }

    @Override
    public boolean exists(Predicate predicate) {
        return createQuery(predicate).fetchCount() > 0;
    }

    protected JPQLQuery<T> createQuery() {
        return from(entityPath);
    }

    protected JPQLQuery<?> createQuery(EntityPath<?> ... paths) {
        return from(paths);
    }

    protected JPQLQuery<T> createQuery(Predicate ... predicates) {
        return createQuery().where(predicates);
    }



    /**
     * Executes the given {@link JPQLQuery} after applying the given {@link Sort}.
     *
     * @param query must not be {@literal null}.
     * @param sort must not be {@literal null}.
     * @return
     */
    private List<T> executeSorted(JPQLQuery<T> query, Sort sort) {
        return getQuerydsl().applySorting(sort, query).fetch();
    }
}
