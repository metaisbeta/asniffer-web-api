package io.github.phillima.avisualizer.repository;

import io.github.phillima.avisualizer.entity.ErrorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ErrorRepository extends MongoRepository<ErrorEntity, UUID> {

}
