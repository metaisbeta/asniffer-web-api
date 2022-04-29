package io.github.phillima.avisualizer.service;

import io.github.phillima.avisualizer.entity.AvisualizerEntity;
import io.github.phillima.avisualizer.entity.ErrorEntity;
import io.github.phillima.avisualizer.model.AvisualizerModel;
import io.github.phillima.avisualizer.model.ErrorModel;
import io.github.phillima.avisualizer.repository.AvisualizerRepository;
import io.github.phillima.avisualizer.repository.ErrorRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AvisualizerService {

    @Autowired
    private AvisualizerRepository repository;

    @Autowired
    private ErrorRepository errorRepository;

    private static final String CV_DEFAULT = "SpaceWeatherTSI-CV.json";
    private static final String PV_DEFAULT = "SpaceWeatherTSI-PV.json";
    private static final String SV_DEFAULT = "SpaceWeatherTSI-SV.json";

    private static final String DEFAULT_NAME = "SpaceWeatherTSI";

    private static final int MAX_CONSULT = 2;

    private AvisualizerModel currentModel = standardResponse();

    public AvisualizerService() throws URISyntaxException, IOException {
    }

    public AvisualizerModel standardResponse() throws URISyntaxException, IOException {
        String cv = readCVFileFromResources(CV_DEFAULT);
        String pv = readCVFileFromResources(PV_DEFAULT);
        String sv = readCVFileFromResources(SV_DEFAULT);

        return new AvisualizerModel(DEFAULT_NAME, cv, pv, sv);
    }

    public String returnFileSV(String fileName) throws URISyntaxException, IOException {
        return readSVFileFromResources(fileName);
    }

    public String returnFileCV(String fileName) throws URISyntaxException, IOException {
        return readCVFileFromResources(fileName);
    }

    public String returnFilePV(String fileName) throws URISyntaxException, IOException {
        return readPVFileFromResources(fileName);
    }

    public static String readSVFileFromResources(String filename) throws URISyntaxException, IOException {
        URL resource = AvisualizerService.class.getClassLoader().getResource(filename);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        return new String(bytes);
    }

    public static String readPVFileFromResources(String filename) throws URISyntaxException, IOException {
        URL resource = AvisualizerService.class.getClassLoader().getResource(filename);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        return new String(bytes);
    }

    public static String readCVFileFromResources(String filename) throws URISyntaxException, IOException {
        URL resource = AvisualizerService.class.getClassLoader().getResource(filename);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        return new String(bytes);
    }

    public String returnSV(String projectID) throws URISyntaxException, IOException {
        if(StringUtils.isEmpty(projectID)){
            return returnFileSV(SV_DEFAULT);
        }
        Optional<AvisualizerEntity> response= this.repository.findById(projectID);
        if(response.isPresent()){
            return response.get().getSv();
        }
        return "";
    }

    public String returnCV(String projectID) throws URISyntaxException, IOException {
        if(StringUtils.isEmpty(projectID)){
            return returnFileSV(SV_DEFAULT);
        }
        Optional<AvisualizerEntity> response= this.repository.findById(projectID);
        if(response.isPresent()){
            return response.get().getCv();
        }
        return "";
    }

    public String returnPV(String projectID) throws URISyntaxException, IOException {
        if(StringUtils.isEmpty(projectID)){
            return returnFileSV(SV_DEFAULT);
        }
        Optional<AvisualizerEntity> response= this.repository.findById(projectID);
        if(response.isPresent()){
            return response.get().getPv();
        }
        return "";
    }

    public AvisualizerEntity saveModel(AvisualizerModel model){
        String cvHash = DigestUtils.sha256Hex(model.getCv());
        List<AvisualizerEntity> responseHash = this.repository.findByHash(cvHash);
        if (!responseHash.isEmpty()){
            return responseHash.get(0);
        }

        List<AvisualizerEntity> responseName = this.repository.findByName(model.getName());

        AvisualizerEntity entity = new AvisualizerEntity();
        entity.setId(model.getName() + "-" + responseName.size());
        entity.setName(model.getName());

        entity.setCv(model.getCv());
        entity.setSv(model.getSv());
        entity.setPv(model.getPv());

        entity.setHash(cvHash);
        entity.setPersist(true);
        entity.setConsults(0);

        entity.setLast_update(LocalDateTime.now());

        return this.repository.save(entity);
    }

    public AvisualizerEntity saveModelTemporary(AvisualizerModel model){
        AvisualizerEntity entity = new AvisualizerEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setName(model.getName());

        entity.setCv(model.getCv());
        entity.setSv(model.getSv());
        entity.setPv(model.getPv());

        entity.setHash("");
        entity.setPersist(false);
        entity.setConsults(0);

        entity.setLast_update(LocalDateTime.now());

        return this.repository.save(entity);
    }

    public AvisualizerEntity getAllInformation(String projectID) throws Exception {
        try {
            Optional<AvisualizerEntity> response = this.repository.findById(projectID);
            if (response.isPresent()) {
                Long consults = response.get().getConsults();
                response.get().setConsults(consults + 1);

                if (!response.get().isPersist() && (consults + 1) >= MAX_CONSULT) {
                    this.repository.delete(response.get());
                    return response.get();
                }

                response.get().setLast_update(LocalDateTime.now());
                this.repository.save(response.get());
                return response.get();
            }
            return new AvisualizerEntity();
        } catch (Exception e){
            throw new Exception("Error during Database consult.");
        }
    }

    public void saveError(ErrorModel model){
        ErrorEntity entity = new ErrorEntity();

        entity.setId(UUID.randomUUID());
        entity.setOs(model.getOs());
        entity.setProject_name(model.getProject_name());
        entity.setError_message(model.getError_message());
        entity.setLast_update(LocalDateTime.now());
        this.errorRepository.save(entity);
        return;
    }

}
