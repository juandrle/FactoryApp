package de.swtpro.factorybuilder.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.repository.FactoryRepository;
import de.swtpro.factorybuilder.repository.GridRepository;
import de.swtpro.factorybuilder.repository.ModelRepository;
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
    private ModelRepository modelRepository;

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
}