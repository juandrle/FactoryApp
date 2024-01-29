package de.swtpro.factorybuilder.service.model.transportation;

import de.swtpro.factorybuilder.entity.Model;
import de.swtpro.factorybuilder.entity.model.AbstractModel;
import de.swtpro.factorybuilder.entity.model.transportation.AbstractTransportation;
import de.swtpro.factorybuilder.service.FieldService;
import de.swtpro.factorybuilder.service.model.AbstractModelService;
import de.swtpro.factorybuilder.utility.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransportationService{
    CurvedPipeService curvedPipeService;
    StraightPipeService straightPipeService;
    ThreeWaySwitchService threeWaySwitchService;
    TwoWaySwitchService twoWaySwitchService;
    @Autowired
    public TransportationService(CurvedPipeService curvedPipeService, StraightPipeService straightPipeService,
                                 ThreeWaySwitchService threeWaySwitchService, TwoWaySwitchService twoWaySwitchService) {
        this.curvedPipeService = curvedPipeService;
        this.straightPipeService = straightPipeService;
        this.threeWaySwitchService = threeWaySwitchService;
        this.twoWaySwitchService = twoWaySwitchService;
    }
    public AbstractTransportation createPlacedModel(Model model, Position rootPosition, long factoryID) {
        try {
            switch (model.getName()) {
                case "pipe_curved" -> {
                    return curvedPipeService.createPlacedModel(rootPosition, factoryID);
                }
                case "pipe_straight" -> {
                    return straightPipeService.createPlacedModel(rootPosition, factoryID);
                }
                case "???" -> {
                    return twoWaySwitchService.createPlacedModel(rootPosition, factoryID);
                }
                case "!!!" -> {
                    return threeWaySwitchService.createPlacedModel(rootPosition, factoryID);
                }
                default -> throw new IllegalArgumentException("Invalid model name");
            }
        } catch (IllegalArgumentException e) {
            // TODO: REAL ERROR HANDLING
            throw new RuntimeException(e);
        }
    }
}
