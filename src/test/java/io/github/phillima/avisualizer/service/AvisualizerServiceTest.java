package io.github.phillima.avisualizer.service;

import io.github.phillima.avisualizer.entity.AvisualizerEntity;
import io.github.phillima.avisualizer.entity.ErrorEntity;
import io.github.phillima.avisualizer.model.AvisualizerModel;
import io.github.phillima.avisualizer.model.ErrorModel;
import io.github.phillima.avisualizer.repository.AvisualizerRepository;
import io.github.phillima.avisualizer.repository.ErrorRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
public class AvisualizerServiceTest {

    @Autowired
    private AvisualizerService service;

    @MockBean
    private AvisualizerRepository repository;

    @MockBean
    private ErrorRepository errorRepository;

    private final String mockProjectName = "project-name";
    private final String mockProjectErrorName = "project-error";

    @Before
    public void setUp() throws Exception {
        repository = mock(AvisualizerRepository.class);
        errorRepository = mock(ErrorRepository.class);
    }

    @Test
    @DisplayName("consultProjectSuccess")
    public void consultProjectSuccess()
            throws Exception {

        int consults = 5;
        AvisualizerEntity entity = mockEntity();
        entity.setConsults(consults);

        doReturn(Optional.of(entity)).when(repository).findById(mockProjectName);
        doReturn(null).when(repository).save(entity);

        AvisualizerEntity response = service.getAllInformation(mockProjectName);

        Assertions.assertEquals(response.getName(), mockProjectName);
        Assertions.assertEquals(response.getId(), mockProjectName);
        Assertions.assertEquals(response.getConsults(), consults+1);
        Assertions.assertEquals(response.getHash(), entity.getHash());
        Assertions.assertEquals(response.getCv(), entity.getCv());
        Assertions.assertEquals(response.getPv(), entity.getPv());
        Assertions.assertEquals(response.getSv(), entity.getSv());
        Assertions.assertNotNull(response.getLast_update());
    }

    @Test
    @DisplayName("consultProjectSuccess_NotPersist")
    public void consultProjectSuccess_NotPersist() throws Exception {

        AvisualizerEntity entity = mockEntity();
        entity.setPersist(false);

        doReturn(Optional.of(entity)).when(repository).findById(mockProjectName);
        doNothing().when(repository).delete(entity);

        AvisualizerEntity response = service.getAllInformation(mockProjectName);

        Assertions.assertEquals(response.getName(), mockProjectName);
        Assertions.assertEquals(response.getId(), mockProjectName);
    }

    @Test
    @DisplayName("consultProjectSuccess_NotPersist")
    public void consultProjectSuccess_NotFind() throws Exception {

        AvisualizerEntity entity = new AvisualizerEntity();

        doReturn(Optional.of(new AvisualizerEntity())).when(repository).findById(mockProjectName);

        AvisualizerEntity response = service.getAllInformation(mockProjectName);

        Assertions.assertEquals(response.getName(), entity.getName());
        Assertions.assertEquals(response.getId(), entity.getId());
    }

    @Test
    @DisplayName("consultProjectError_NotPersist")
    public void consultProjectError() throws Exception {
        AvisualizerEntity response = service.getAllInformation(mockProjectName);

        Assertions.assertNull(response.getId());
        Assertions.assertNull(response.getName());
        Assertions.assertNull(response.getCv());
    }

    @Test
    @DisplayName("saveProjectSuccess_AlreadySaved")
    public void saveProjectSuccess_AlreadySaved() throws Exception {
        AvisualizerModel model = mockModel();
        AvisualizerEntity entity = mockEntity();

        doReturn(List.of(entity)).when(repository).findByHash(mockProjectName);
        doReturn(entity).when(repository).save(any());
        AvisualizerEntity response = service.saveModel(model);

        Assertions.assertEquals(response.getName(), mockProjectName);
        Assertions.assertEquals(response.getId(), mockProjectName);
    }

    @Test
    @DisplayName("saveProjectSuccess_NewProject")
    public void saveProjectSuccess_NewProject() {
        AvisualizerModel model = mockModel();
        AvisualizerEntity entity = mockEntity();

        doReturn(Collections.<AvisualizerEntity>emptyList()).when(repository).findByHash(mockProjectName);
        doReturn(entity).when(repository).save(any());
        AvisualizerEntity response = service.saveModel(model);

        Assertions.assertEquals(response.getName(), model.getName());
        Assertions.assertEquals(response.getSv(), model.getSv());
        Assertions.assertEquals(response.getPv(), model.getPv());
        Assertions.assertEquals(response.getCv(), model.getCv());
    }

    @Test
    @DisplayName("saveProjectSuccess_DontPersist")
    public void saveProjectSuccess_DontPersist() throws Exception {
        AvisualizerModel model = mockModel();
        AvisualizerEntity entity = mockEntity();

        doReturn(entity).when(repository).save(any());
        AvisualizerEntity response = service.saveModelTemporary(model);

        Assertions.assertEquals(response.getName(), model.getName());
        Assertions.assertEquals(response.getSv(), model.getSv());
        Assertions.assertEquals(response.getPv(), model.getPv());
        Assertions.assertEquals(response.getCv(), model.getCv());
    }

    @Test
    @DisplayName("saveError")
    public void saveError() throws Exception {
        ErrorModel model = new ErrorModel("Windows",mockProjectName,"stack_trace");
        ErrorEntity entity = new ErrorEntity(UUID.randomUUID(),mockProjectName,"Windows","stack_trace",LocalDateTime.now());

        doReturn(entity).when(errorRepository).save(any());
        service.saveError(model);
    }



    public AvisualizerEntity mockEntity() {
        AvisualizerEntity entity = new AvisualizerEntity();
        entity.setConsults(3);
        entity.setCv("cv");
        entity.setHash("12");
        entity.setPersist(true);
        entity.setLast_update(LocalDateTime.of(2022, Month.APRIL,1,10,15,30));
        entity.setPv("pv");
        entity.setSv("sv");
        entity.setId(mockProjectName);
        entity.setName(mockProjectName);
        return entity;
    }

    public AvisualizerModel mockModel(){
        return new AvisualizerModel(mockProjectName,"cv","sv","pv");
    }
}
