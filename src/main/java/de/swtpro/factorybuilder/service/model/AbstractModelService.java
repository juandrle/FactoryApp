package de.swtpro.factorybuilder.service.model;

import de.swtpro.factorybuilder.entity.*;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.repository.AbstractModelRepository;
import de.swtpro.factorybuilder.service.model.machine.MachineService;
import de.swtpro.factorybuilder.service.model.other.OtherService;
import de.swtpro.factorybuilder.service.model.transportation.TransportationService;
import de.swtpro.factorybuilder.utility.Position;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbstractModelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelService.class);
    AbstractModelRepository abstractModelRepository;
    TransportationService transportationService;
    OtherService otherService;
    MachineService machineService;

    @Autowired
    AbstractModelService(AbstractModelRepository abstractModelRepository,@Lazy TransportationService transportationService,
                         @Lazy OtherService otherService,@Lazy  MachineService machineService) {
        this.abstractModelRepository = abstractModelRepository;
        this.transportationService = transportationService;
        this.otherService = otherService;
        this.machineService = machineService;
    }

    public Optional<AbstractModel> getPlacedModelById(long id) {
        return abstractModelRepository.findById(id);
    }
    public void deleteById(long id) {
        abstractModelRepository.deleteById(id);
    }
//    protected boolean fillPlacedModelLists(AbstractModel abstractModel) {
//        Position rootPos = abstractModel.getRootPos();
//        Factory factory = abstractModel.getFactory();
//        long factoryID = factory.getFactoryID();
//        if (factory == null) return false;
//
//        List<Field> placedFields = new ArrayList<>();
//        List<Input> placedInputs = new ArrayList<>();
//        List<Output> placedOutputs = new ArrayList<>();
//        Field f;
//
//        try {
//            // add fields to placedModel list
//            switch (abstractModel.getName().toLowerCase()) {
//                // machines
//                case "montagemaschine_gross":
//                    // deprecated
//                    for (int i = 0; i < 3; i++) {
//                        for (int j = 0; j < 3; j++) {
//                            for (int k = 0; k < 2; k++)
//                                placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + i, rootPos.getY() + j, rootPos.getZ() + k), factoryID).orElseThrow());
//                        }
//                    }
//                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() + 1, rootPos.getZ() + 3), factoryID).orElseThrow());
//                    break;
//                case "montagemaschine_klein":
//                    // deprecated
//                    for (int i = 0; i < 3; i++)
//                        placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + i, rootPos.getZ()), factoryID).orElseThrow());
//                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() - 1, rootPos.getZ()), factoryID).orElseThrow());
//                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() - 1, rootPos.getZ()), factoryID).orElseThrow());
//                    placedFields.add(fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY(), rootPos.getZ() + 1), factoryID).orElseThrow());
//                    break;
//
//            }
//
//            // add in- and output position and orientation to lists
//            switch (abstractModel.getName().toLowerCase()) {
//                // machines
//                case "montagemaschine_gross":
//                    // input
//                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
//                    placedInputs.add(createInput(f, "West"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
//                    placedInputs.add(createInput(f, "West"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
//                    placedInputs.add(createInput(f, "South"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
//                    placedInputs.add(createInput(f, "South"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY() + 2, rootPos.getZ()), factoryID).orElseThrow();
//                    placedInputs.add(createInput(f, "South"));
//
//                    // output
//                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "North"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
//                    placedOutputs.add(createOutput(f, "North"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + 2, rootPos.getZ()), factoryID).orElseThrow();
//                    placedOutputs.add(createOutput(f, "North"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY() + 2, rootPos.getZ()), factoryID).orElseThrow();
//                    placedOutputs.add(createOutput(f, "East"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() + 2, rootPos.getZ()), factoryID).orElseThrow();
//                    placedOutputs.add(createOutput(f, "East"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY() + 2, rootPos.getZ()), factoryID).orElseThrow();
//                    placedOutputs.add(createOutput(f, "East"));
//                    break;
//                case "montagemaschine_klein":
//                    // deprecated
//                    // input
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX(), rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
//                    placedInputs.add(createInput(f, "West"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() - 1, rootPos.getZ()), factoryID).orElseThrow();
//                    placedInputs.add(createInput(f, "North"));
//                    // output
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 1, rootPos.getY() + 1, rootPos.getZ()), factoryID).orElseThrow();
//                    placedOutputs.add(createOutput(f, "South"));
//                    f = fieldService.getFieldByPosition(createNewPosition(rootPos.getX() + 2, rootPos.getY(), rootPos.getZ()), factoryID).orElseThrow();
//                    placedOutputs.add(createOutput(f, "East"));
//                    break;
//                // transportation
//                /* case "switch_3_2_in_ws":
//                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
//                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "South"));
//                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "East"));
//                    break;
//                case "switch_3_2_in_es":
//                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "East"));
//                    placedInputs.add(createInput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "South"));
//                    placedOutputs.add(createOutput(fieldService.getFieldByPosition(rootPos, factoryID).orElseThrow(), "West"));
//                    break; */
//            }
//
//            abstractModel.getPlacedFields().addAll(placedFields);
//            abstractModel.getInputs().addAll(placedInputs);
//            abstractModel.getOutputs().addAll(placedOutputs);
//
//            for (Field placedField : abstractModel.getPlacedFields()) {
//                LOGGER.info("Placing model at position: " + placedField.getPosition().toString());
//            }
//
//            return true;
//        } catch (NoSuchElementException e) {
//            LOGGER.error("Field not found. Out of bounds.");
//            return false;
//        }
//    }
    @Transactional
    public AbstractModel createPlacedModel(Model model, Position rootPosition, long factoryID) {
        if (model != null && rootPosition != null) {
            try {
                switch (model.getType()) {
                    case MACHINE -> {
                        return abstractModelRepository.save(machineService.createPlacedModel(model, rootPosition, factoryID));
                    }
                    case OTHER -> {
                        return abstractModelRepository.save(otherService.createPlacedModel(model, rootPosition, factoryID));
                    }
                    case TRANSPORT -> {
                        return abstractModelRepository.save(transportationService.createPlacedModel(model, rootPosition, factoryID));
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Factory with Id: " + factoryID + " doesn't exist", e);
                return null;
            }
        }
        LOGGER.error("Placing model NOT successfull");
        return null;
    }

    public List<AbstractModel> findAllByFactoryId(Factory factory) {
        return abstractModelRepository.findByFactory(factory);
    }

}
