package com.organization.gsoc.Repository;

import com.organization.gsoc.DTO.OrganizationFilterDTO;
import com.organization.gsoc.Entity.OrganizationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrganizationRepositoryCustomImpl
        implements OrganizationRepositoryCustom {

    private final EntityManager entityManager;

    public OrganizationRepositoryCustomImpl(
            EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<OrganizationEntity> searchOrganizations(
            OrganizationFilterDTO filter,
            Pageable pageable
    ) {

        StringBuilder where = new StringBuilder(
                " WHERE 1 = 1 "
        );

        List<Object> parameters = new ArrayList<>();

        /*
         * =====================================================
         * ORGANIZATION NAME
         * =====================================================
         */

        if (filter.orgName() != null
                && !filter.orgName().isBlank()) {

            where.append("""
                    AND LOWER(o.name) LIKE LOWER(?)
                    """);

            parameters.add(
                    "%" + filter.orgName().trim() + "%"
            );
        }

        /*
         * =====================================================
         * ACTIVE ORGANIZATION
         * =====================================================
         */

        if (filter.activeOrg() != null) {

            where.append("""
                    AND o.active_org = ?
                    """);

            parameters.add(filter.activeOrg());
        }

        /*
         * =====================================================
         * YEARS
         *
         * [2016, 2024]
         *
         * means:
         *
         * has 2016
         * AND
         * has 2024
         * =====================================================
         */

        if (filter.years() != null
                && !filter.years().isEmpty()) {

            where.append("""
                    AND o.id IN (
                        SELECT oy.organization_id
                        FROM organization_years oy
                        WHERE oy.year IN (
                    """);

            appendPlaceholders(
                    where,
                    filter.years().size()
            );

            where.append("""
                        )
                        GROUP BY oy.organization_id
                        HAVING COUNT(DISTINCT oy.year) = ?
                    )
                    """);

            parameters.addAll(filter.years());

            parameters.add(
                    filter.years().size()
            );
        }

        /*
         * =====================================================
         * TECHNOLOGIES
         *
         * [Kotlin, Java]
         *
         * means:
         *
         * has Kotlin
         * AND
         * has Java
         * =====================================================
         */

        if (filter.technologies() != null
                && !filter.technologies().isEmpty()) {

            where.append("""
                    AND o.id IN (
                        SELECT ot.organization_id
                        FROM organization_technologies ot
                        JOIN technologies t
                            ON t.id = ot.technology_id
                        WHERE LOWER(t.name) IN (
                    """);

            appendPlaceholders(
                    where,
                    filter.technologies().size()
            );

            where.append("""
                        )
                        GROUP BY ot.organization_id
                        HAVING COUNT(DISTINCT LOWER(t.name)) = ?
                    )
                    """);

            for (String technology : filter.technologies()) {
                parameters.add(
                        technology.trim().toLowerCase()
                );
            }

            parameters.add(
                    filter.technologies().size()
            );
        }

        /*
         * =====================================================
         * CATEGORIES
         *
         * [Web, Database]
         *
         * means:
         *
         * has Web
         * AND
         * has Database
         * =====================================================
         */

        if (filter.categories() != null
                && !filter.categories().isEmpty()) {

            where.append("""
                    AND o.id IN (
                        SELECT oc.organization_id
                        FROM organization_categories oc
                        JOIN categories c
                            ON c.id = oc.category_id
                        WHERE LOWER(c.name) IN (
                    """);

            appendPlaceholders(
                    where,
                    filter.categories().size()
            );

            where.append("""
                        )
                        GROUP BY oc.organization_id
                        HAVING COUNT(DISTINCT LOWER(c.name)) = ?
                    )
                    """);

            for (String category : filter.categories()) {
                parameters.add(
                        category.trim().toLowerCase()
                );
            }

            parameters.add(
                    filter.categories().size()
            );
        }

        /*
         * =====================================================
         * TOPICS
         *
         * [AI, Cloud]
         *
         * means:
         *
         * has AI
         * AND
         * has Cloud
         * =====================================================
         */

        if (filter.topics() != null
                && !filter.topics().isEmpty()) {

            where.append("""
                    AND o.id IN (
                        SELECT ot.organization_id
                        FROM organization_topics ot
                        JOIN topics t
                            ON t.id = ot.topic_id
                        WHERE LOWER(t.name) IN (
                    """);

            appendPlaceholders(
                    where,
                    filter.topics().size()
            );

            where.append("""
                        )
                        GROUP BY ot.organization_id
                        HAVING COUNT(DISTINCT LOWER(t.name)) = ?
                    )
                    """);

            for (String topic : filter.topics()) {
                parameters.add(
                        topic.trim().toLowerCase()
                );
            }

            parameters.add(
                    filter.topics().size()
            );
        }

        /*
         * =====================================================
         * DATA QUERY
         * =====================================================
         */

        String dataSql = """
                SELECT o.*
                FROM organizations o
                """
                + where
                + getOrderBy(filter)
                + " LIMIT ? OFFSET ?";

        Query dataQuery = entityManager.createNativeQuery(
                dataSql,
                OrganizationEntity.class
        );

        setParameters(dataQuery, parameters);

        int nextParameter = parameters.size() + 1;

        dataQuery.setParameter(
                nextParameter,
                pageable.getPageSize()
        );

        dataQuery.setParameter(
                nextParameter + 1,
                pageable.getOffset()
        );

        List<OrganizationEntity> organizations =
                dataQuery.getResultList();

        /*
         * =====================================================
         * COUNT QUERY
         * =====================================================
         */

        String countSql = """
                SELECT COUNT(*)
                FROM organizations o
                """
                + where;

        Query countQuery =
                entityManager.createNativeQuery(countSql);

        setParameters(countQuery, parameters);

        Number total =
                (Number) countQuery.getSingleResult();

        return new PageImpl<>(
                organizations,
                pageable,
                total.longValue()
        );
    }

    /*
     * =========================================================
     * PLACEHOLDERS
     *
     * Example:
     *
     * count = 3
     *
     * ?, ?, ?
     * =========================================================
     */

    private void appendPlaceholders(
            StringBuilder sql,
            int count
    ) {

        for (int i = 0; i < count; i++) {

            if (i > 0) {
                sql.append(", ");
            }

            sql.append("?");
        }
    }

    /*
     * =========================================================
     * SET PARAMETERS
     * =========================================================
     */

    private void setParameters(
            Query query,
            List<Object> parameters
    ) {

        for (int i = 0; i < parameters.size(); i++) {

            query.setParameter(
                    i + 1,
                    parameters.get(i)
            );
        }
    }

    /*
     * =========================================================
     * SORT
     * =========================================================
     */

    private String getOrderBy(
            OrganizationFilterDTO filter
    ) {

        String direction =
                filter.sortDirection().name().equals("DESC")
                        ? "DESC"
                        : "ASC";

        return " ORDER BY o.name " + direction + " ";
    }
}