package com.emmanuel.sarabrandserver.clientz.repository;

import com.emmanuel.sarabrandserver.clientz.projection.ClientzPojo;
import com.emmanuel.sarabrandserver.clientz.entity.Clientz;
import com.emmanuel.sarabrandserver.enumeration.RoleEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientzRepository extends JpaRepository<Clientz, Long> {
    @Query(value = "SELECT c FROM Clientz c WHERE c.email = :principal OR c.username = :principal")
    Optional<Clientz> findByPrincipal(@Param(value = "principal") String principal);

    @Query(value = """
    SELECT COUNT (c.clientId)
    FROM Clientz c
    WHERE c.email = :email OR c.username = :username
    """)
    int principalExists(@Param(value = "email") String email, @Param(value = "username") String username);

    @Query(value = """
    SELECT c
    FROM Clientz c
    WHERE c.email = :email OR c.username = :username
    """)
    Optional<Clientz> workerExists(@Param(value = "email") String email, @Param(value = "username") String username);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE Clientz c SET c.accountNoneLocked = :bool WHERE c.clientId = :id")
    void lockClientAccount(@Param(value = "bool") boolean bool, @Param(value = "id") long id);

    @Query(value = """
    SELECT COUNT(c.clientId) FROM Clientz c
    JOIN ClientRole r ON c.clientId = r.clientz.clientId
    WHERE (c.email = :email OR c.username = :username)
    AND r.role = :role
    """)
    int isAdmin(
            @Param(value = "email") String email,
            @Param(value = "username") String username,
            @Param(value = "role") RoleEnum role
    );

    @Query(value = """
    SELECT c.firstname AS name FROM Clientz c
    INNER JOIN ClientRole r ON c.clientId = r.clientz.clientId
    WHERE r.role = :role
    AND c.accountNoneLocked = TRUE
    AND c.enabled = TRUE
    AND c.accountNonExpired = TRUE
    AND c.credentialsNonExpired = TRUE
    """)
    List<ClientzPojo> fetchAll(@Param(value = "role") String role, Pageable request);
}
