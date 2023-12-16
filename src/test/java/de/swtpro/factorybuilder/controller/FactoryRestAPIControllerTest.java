package de.swtpro.factorybuilder.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.swtpro.factorybuilder.entity.Factory;
import de.swtpro.factorybuilder.service.FactoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class FactoryRestAPIControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FactoryService factoryService;

    @InjectMocks
    private FactoryRestAPIController factoryRestAPIController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(factoryRestAPIController).build();
    }

    @Test
    public void testSave() throws Exception {
        // Assuming Factory's ID is generated and returned
        Factory mockFactory = new Factory();
        long expectedId = 0L;
        when(factoryService.saveFactory(any())).thenReturn(mockFactory);

        mockMvc.perform(post("/api/factory/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Factory\", \"password\":\"pass123\", \"width\":10, \"depth\":20, \"height\":30}"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedId)));
    }

    @Test
    public void testDelete() throws Exception {
        long idToDelete = 1L;
        doNothing().when(factoryService).deleteFactoryById(idToDelete);

        mockMvc.perform(post("/api/factory/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(idToDelete)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
