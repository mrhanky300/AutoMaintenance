package io.automaintenance.api.vehicle;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.garage.GarageEntity;
import io.automaintenance.api.repos.GarageRepository;
import io.automaintenance.api.repos.VehicleRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class AddVehicleControllerTest {
    @Mock
    VehicleRepository vehicleRepository;

    @Mock
    GarageRepository garageRepository;

    @Mock
    VehicleResponseMapper vehicleResponseMapper;

    @InjectMocks
    VehicleController vehicleController;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    MockMvc mockMvc;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setupMock() {
        mockMvc = standaloneSetup(vehicleController)
                .build();
    }

    @Test
    public void addNewVehicleToGarage() throws Exception {
        GarageEntity garageEntity = new GarageEntity();
        garageEntity.setGarageId("id");
        garageEntity.setGarageName("testName");


        GarageEntity expectedGarageEntity = new GarageEntity();
        when(garageRepository.findOne(garageEntity.getGarageId()))
                .thenReturn(expectedGarageEntity);

        VehicleRequest vehicleRequest = new VehicleRequest();
        VehicleEntity vehicleEntity = new VehicleEntity();
        String garageId = "garageId";
        String vehicleId = "vehicleId";
        when(vehicleResponseMapper.map(any(VehicleRequest.class), anyString(), anyString())).thenReturn(vehicleEntity);

        mockMvc.perform(post("/garages/{garageId}/vehicles", garageEntity.getGarageId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated());

        verify(vehicleRepository, times(1)).save(Matchers.isA(VehicleEntity.class));
        verifyNoMoreInteractions(vehicleRepository);
    }

    @Test
    public void addNewVehicleWithUnknownGarage_throwsRNFException() throws Exception {
        GarageEntity garageEntity = new GarageEntity();
        when(garageRepository.findOne(garageEntity.getGarageId())).thenReturn(null);

        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Garage not found");

        mockMvc.perform(post("/garages/{garageId}/vehicles", "id")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void addNewVehicleWithMissingGarage_throwsRNFException() throws Exception {
        expectedException.expectCause(isA(ResourcesNotFoundException.class));
        expectedException.expectMessage("Garage is required");

        mockMvc.perform(post("/garages//vehicles"));
    }
}
