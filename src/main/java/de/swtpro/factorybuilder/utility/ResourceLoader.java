package de.swtpro.factorybuilder.utility;

import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.service.ModelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Component
public class ResourceLoader implements CommandLineRunner {
    private final ModelService modelService;


    ResourceLoader(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public void run(String... args) {
        getAllModelsByPathAndTypes("/static/models", ModelType.MACHINE, ModelType.OTHER,
                ModelType.ITEM_PRODUCT, ModelType.ITEM_RESOURCE, ModelType.ITEM_PROCESSED, ModelType.TRANSPORT);

    }

    public void getAllModelsByPathAndTypes(String directoryPath, ModelType... types) {
        try {
            List<Model> models = new ArrayList<>();
            for (ModelType type : types) {
                switch (type) {
                    case MACHINE:
                        models.addAll(pathToModelListAndIconFileName(directoryPath, "/machines", ModelType.MACHINE, "machine.png"));
                        break;
                    case OTHER:
                        models.addAll(pathToModelListAndIconFileName(directoryPath, "/other", ModelType.OTHER, "other.png"));
                        break;
                    case ITEM_PRODUCT:
                        models.addAll(pathToModelListAndIconFileName(directoryPath, "/items/products", ModelType.ITEM_PRODUCT, "other.png"));
                        break;
                    case ITEM_RESOURCE:
                        models.addAll(pathToModelListAndIconFileName(directoryPath, "/items/resources", ModelType.ITEM_RESOURCE, "other.png"));
                        break;
                    case ITEM_PROCESSED:
                        models.addAll(pathToModelListAndIconFileName(directoryPath, "/items/processed", ModelType.ITEM_PROCESSED, "other.png"));
                        break;
                    case TRANSPORT:
                        models.addAll(pathToModelListAndIconFileName(directoryPath, "/transportation", ModelType.TRANSPORT, "transport.png"));
                        break;
                }
            }
            modelService.saveAllModels(models);
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }

    }

    private List<Model> pathToModelListAndIconFileName(String directoryPath, String type, ModelType modelType, String fileName) throws IOException {
        List<Model> models = new ArrayList<>();
        Path path = Paths.get(new ClassPathResource(directoryPath + type).getURI());
        try (Stream<Path> paths = Files.walk(path)) {
            paths.forEach(e -> {
                Path relativePath = path.relativize(e);
                String filePath = "/models" + type + "/" + relativePath.toString().replace(File.separator, "/");
                String[] suffixPaths = type.split("/");
                if (!filePath.endsWith(suffixPaths[suffixPaths.length - 1] + "/")) {
                    Model m = new Model();
                    m.setModelFile(filePath);
                    m.setIcon("/icons/models/" + fileName);
                    m.setName(getNameByPath(filePath));
                    m.setType(modelType);
                    models.add(m);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return models;
    }

    private String getNameByPath(String path) {
        String[] pathInPartsWithoutSuffix = path.split("\\.")[0].split("/");
        return pathInPartsWithoutSuffix[pathInPartsWithoutSuffix.length - 1];
    }
}
