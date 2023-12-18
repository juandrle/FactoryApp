package de.swtpro.factorybuilder.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.entity.Field;
import de.swtpro.factorybuilder.repository.FactoryRepository;
import de.swtpro.factorybuilder.repository.GridRepository;
import de.swtpro.factorybuilder.repository.PlacedModelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FactoryServiceTest {
    // for future tests
    @Mock
    private GridRepository gridRepository;
    // for future tests
    @Mock
    private PlacedModelRepository placedModelRepository;

    @Mock
    private FactoryRepository factoryRepository;

    @InjectMocks
    private FactoryService factoryService;

    @Test
    public void testGetFactoryByID() {
        long id = 1L;
        Factory expectedFactory = new Factory();
        when(factoryRepository.findById(id)).thenReturn(Optional.of(expectedFactory));

        Factory result = factoryService.getFactoryByID(id);

        assertEquals(expectedFactory, result);
        verify(factoryRepository).findById(id);
    }

    @Test
    public void testSaveFactory() {
        Factory factoryToSave = new Factory();
        when(factoryRepository.save(factoryToSave)).thenReturn(factoryToSave);

        Factory result = factoryService.saveFactory(factoryToSave);

        assertEquals(factoryToSave, result);
        verify(factoryRepository).save(factoryToSave);
    }

    @Test
    public void testDeleteFactoryById() {
        long idToDelete = 1L;

        factoryService.deleteFactoryById(idToDelete);

        verify(factoryRepository).deleteById(idToDelete);
    }
    @Test
    public void testInitializeField() {
        long factoryID = 0L;
        int height = 2, width = 3, depth = 4;
        Factory mockFactory = mock(Factory.class);
        when(mockFactory.getHeight()).thenReturn(height);
        when(mockFactory.getWidth()).thenReturn(width);
        when(mockFactory.getDepth()).thenReturn(depth);

        when(factoryService.getFactoryById(factoryID)).thenReturn(Optional.of(mockFactory));

        factoryService.initializeField(factoryID);

        verify(gridRepository, times(height * width * depth)).save(any(Field.class));
    }
}