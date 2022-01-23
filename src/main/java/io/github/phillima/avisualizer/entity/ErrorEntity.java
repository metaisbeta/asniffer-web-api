package io.github.phillima.avisualizer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("error")
public class ErrorEntity {

    @Id
    private UUID id;
    private String project_name;
    private String os;
    private String error_message;

    private LocalDateTime last_update;

}
