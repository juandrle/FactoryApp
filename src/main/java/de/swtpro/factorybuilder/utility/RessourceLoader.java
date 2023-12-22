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
public class RessourceLoader implements CommandLineRunner {
    private final ModelService modelService;


    RessourceLoader(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public void run(String... args) {
        getAllModelsByPathAndType("/static/models/mock/machines/", "MACHINE");
        getAllModelsByPathAndType("/static/models/mock/other/", "OTHER");

    }

    public void getAllModelsByPathAndType(String directoryPath, String type) {
        try {
            Path path = Paths.get(new ClassPathResource(directoryPath).getURI());
            List<Model> models = new ArrayList<>();
            try (Stream<Path> paths = Files.walk(path)) {
                paths.forEach(e -> {
                    String[] fullPath = e.toString().split("/");
                    switch (type) {
                        case "MACHINE":
                            String filePathMachine = "/models/mock/machines/" + fullPath[fullPath.length - 1].toString();
                            if (!filePathMachine.endsWith("machines")) {
                                Model m = new Model();
                                m.setModelFile(filePathMachine);
                                m.setIcon("/icons/accessibility-outline.svg");
                                m.setType(type);
                                models.add(m);
                            }
                            break;
                        case "OTHER":
                            String filePathOther = "/models/mock/other/" + fullPath[fullPath.length - 1].toString();
                            if (!filePathOther.endsWith("other")) {
                                Model m = new Model();
                                m.setModelFile(filePathOther);
                                m.setIcon("/icons/accessibility-outline.svg");
                                m.setType(type);
                                models.add(m);
                            }
                            break;

                    }
                });
                modelService.saveAllModels(models);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
