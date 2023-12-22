package de.swtpro.factorybuilder.utility;

import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.service.ModelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;



@Component
public class ModelLoader implements CommandLineRunner {
    private final ModelService modelService;


    ModelLoader(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public void run(String... args) {
        listAllPaths("/static/models/mock/");
    }

    public void listAllPaths(String directoryPath) {
        try {
            Path path = Paths.get(new ClassPathResource(directoryPath).getURI());

            List<Model> models = new ArrayList<>();
            try (Stream<Path> paths = Files.walk(path)) {
                paths.forEach(e -> {
                    String [] fullPath = e.toString().split("/");
                    String filePath = "/models/mock/" + fullPath[fullPath.length-1].toString();
                    if (!filePath.endsWith("mock")) {
                        Model m = new Model();
                        m.setModelFile(filePath);
                        m.setIcon("/icons/accessibility-outline.svg");
                        models.add(m);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            modelService.saveAllModels(models);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
