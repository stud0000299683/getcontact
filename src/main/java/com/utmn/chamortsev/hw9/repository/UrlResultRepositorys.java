package com.utmn.chamortsev.hw9.repository;

import com.utmn.chamortsev.hw9.entity.UrlResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UrlResultRepositorys extends JpaRepository<UrlResultEntity, Long> {
    List<UrlResultEntity> findByUrlEntityIdOrderByProcessedAtDesc(Long urlId);
    List<UrlResultEntity> findAllByOrderByProcessedAtDesc();

    @Query("SELECT ur FROM UrlResultEntity ur JOIN ur.urlEntity u WHERE u.active = true ORDER BY ur.processedAt DESC")
    List<UrlResultEntity> findActiveUrlResults();

    @Query("SELECT AVG(ur.responseTime) FROM UrlResultEntity ur WHERE ur.statusCode = 200")
    Double findAverageResponseTime();

    @Query("SELECT COUNT(ur) FROM UrlResultEntity ur WHERE ur.statusCode = :statusCode")
    long countByStatusCode(@Param("statusCode") Integer statusCode);

    @Query("""
    SELECT COUNT(ur) FROM UrlResultEntity ur JOIN ur.urlEntity u
    WHERE ur.statusCode = 200 AND ur.urlEntity.active = true
        """)
    long countSuccessfulActiveRequests();
}
