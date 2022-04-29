package io.github.phillima.avisualizer.controller;

import io.github.phillima.avisualizer.entity.AvisualizerEntity;
import io.github.phillima.avisualizer.model.AvisualizerModel;
import io.github.phillima.avisualizer.service.AvisualizerService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ControllerTest {

    @Autowired
    private Controller controller;

    @MockBean
    private AvisualizerService avisualizerService;

    private final String mockProjectName = "project-name";
    private final String mockProjectErrorName = "project-error";

    @Before
    public void setUp() throws Exception {
        avisualizerService = mock(AvisualizerService.class);
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("consultProject_Success")
    public void consultProject_Success()
            throws Exception {

        AvisualizerEntity entity = mockEntity();

        doReturn(entity).when(avisualizerService).getAllInformation(mockProjectName);

        ResponseEntity<AvisualizerEntity> returnAllData = controller.returnAllData(mockProjectName);

        Assertions.assertEquals(returnAllData.getStatusCode(), HttpStatus.OK);
        reset(avisualizerService);
    }

    @Test
    @DisplayName("consultProject_Error")
    public void consultProject_Error()
            throws Exception {

        doReturn(null).when(avisualizerService).getAllInformation(mockProjectErrorName);

        ResponseEntity<AvisualizerEntity> returnAllData = controller.returnAllData(mockProjectErrorName);

        Assertions.assertEquals(returnAllData.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        reset(avisualizerService);
    }

    @Test
    @DisplayName("saveProject_Success_Persist")
    public void saveProject_Success_Persist() {

        AvisualizerEntity entity = mockEntity();

        AvisualizerModel model = mockModel();

        doReturn(entity).when(avisualizerService).saveModel(model);

        ResponseEntity<AvisualizerEntity> returnAllData = controller.saveData(model, true);

        Assertions.assertEquals(returnAllData.getStatusCode(), HttpStatus.OK);
        reset(avisualizerService);
    }

    @Test
    @DisplayName("saveProject_Success_NotPersist")
    public void saveProject_Success_NotPersist() {

        AvisualizerEntity entity = mockEntity();

        AvisualizerModel model = mockModel();

        doReturn(entity).when(avisualizerService).saveModel(model);

        ResponseEntity<AvisualizerEntity> returnAllData = controller.saveData(model, false);

        Assertions.assertEquals(returnAllData.getStatusCode(), HttpStatus.OK);
        reset(avisualizerService);
    }


    public AvisualizerEntity mockEntity() {
        AvisualizerEntity entity = new AvisualizerEntity();
        entity.setConsults(1);
        entity.setCv("cv");
        entity.setHash("12");
        entity.setPersist(true);
        entity.setLast_update(LocalDateTime.of(2022, Month.APRIL,1,10,15,30));
        entity.setPv("pv");
        entity.setId("project-name");
        return entity;
    }

    public AvisualizerModel mockModel(){
        return new AvisualizerModel("model","pv","sv","pv");
    }



}
