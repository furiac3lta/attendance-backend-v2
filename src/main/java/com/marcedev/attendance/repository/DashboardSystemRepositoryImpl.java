package com.marcedev.attendance.repository;

import com.marcedev.attendance.enums.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardSystemRepositoryImpl implements DashboardSystemRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long countOrganizations() {
        return em.createQuery(
                "select count(o) from Organization o where o.active = true", Long.class
        ).getSingleResult();
    }

    @Override
    public long countUsers() {
        return em.createQuery(
                "select count(u) from User u where u.active = true", Long.class
        ).getSingleResult();
    }

    @Override
    public long countByRole(Rol role) {
        return em.createQuery(
                        "select count(u) from User u where u.role = :role and u.active = true", Long.class
                )
                .setParameter("role", role)
                .getSingleResult();
    }

    @Override
    public long countCourses() {
        return em.createQuery(
                "select count(c) from Course c where c.active = true", Long.class
        ).getSingleResult();
    }

    @Override
    public long countActiveEnrollments() {
        return em.createQuery(
                "select count(e) from Enrollment e where e.active = true", Long.class
        ).getSingleResult();
    }

    @Override
    public long countPayments() {
        return em.createQuery(
                "select count(p) from Payment p", Long.class
        ).getSingleResult();
    }
}
