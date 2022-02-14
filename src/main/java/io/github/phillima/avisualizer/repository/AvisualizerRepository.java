package io.github.phillima.avisualizer.repository;

import io.github.phillima.avisualizer.entity.AvisualizerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvisualizerRepository extends MongoRepository<AvisualizerEntity, UUID> {

    @Query("{ 'hash' : ?0 }")
    List<AvisualizerEntity> findByHash(String hash);

    @Query("{ 'name' : ?0 }")
    List<AvisualizerEntity> findByName(String name);

}
