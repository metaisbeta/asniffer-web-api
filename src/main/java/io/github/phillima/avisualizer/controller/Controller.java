package io.github.phillima.avisualizer.controller;


import io.github.phillima.avisualizer.entity.AvisualizerEntity;
import io.github.phillima.avisualizer.model.AvisualizerModel;
import io.github.phillima.avisualizer.model.ErrorModel;
import io.github.phillima.avisualizer.service.AvisualizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

@CrossOrigin
@RestController
public class Controller {

    @Autowired
    private AvisualizerService avisualizerService;

    @RequestMapping("/data.json")
    public ResponseEntity<AvisualizerEntity> returnAllData(@RequestParam(required = false, name = "project") String project) throws Exception {
        try {
            AvisualizerEntity response = avisualizerService.getAllInformation(project);
            if (Objects.isNull(response.getId())) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/data/save")
    public ResponseEntity<AvisualizerEntity> saveData(
            @RequestBody AvisualizerModel model,
            @RequestParam(name = "persist", required = false, defaultValue = "true") boolean persist) {
        AvisualizerEntity resp;
        if(persist){
            resp = avisualizerService.saveModel(model);
        } else{
            resp = avisualizerService.saveModelTemporary(model);
        }

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/data/error")
    public ResponseEntity saveError(@RequestBody ErrorModel model) {
        avisualizerService.saveError(model);
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }

}
