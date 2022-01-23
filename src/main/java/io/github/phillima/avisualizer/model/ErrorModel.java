package io.github.phillima.avisualizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorModel {

    private String os;
    private String project_name;
    private String error_message;

}
