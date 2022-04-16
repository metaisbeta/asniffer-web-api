package io.github.phillima.avisualizer.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Document("avisualizer")
public class AvisualizerEntity {

    @Id
    private String id;
    private String name;
    private String hash;

    private String cv;
    private String pv;
    private String sv;

    private boolean persist;
    private long consults;

    private LocalDateTime last_update;
}
