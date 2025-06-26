package com.leegality.e2e;

import com.leegality.atlas.api.StatesApi;
import com.leegality.atlas.api.testdto.request.StateDTO;
import com.leegality.atlas.api.testdto.response.StateResponseDTO;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("State")
@Feature("Create and get state test plan")
public final class CreateAndGetStateTest {

    StatesApi statesApi = new StatesApi();
    List<String> ids = new ArrayList<>();

    @Test(description = "Create and get new state")
    @Description("Should be able to create and get new State")
    @Story("TECH-5600")
    public void createAndGetNewState() {
        StateDTO payload = getCreateStatePayload();

        StateResponseDTO stateResponseDTO =
                statesApi.createState(payload, Response.Status.OK.getStatusCode()).as(StateResponseDTO.class);

        step("Validate Create State Response");
        assertThat(stateResponseDTO.getCode(), equalTo("LE_DST_001"));
        assertThat(stateResponseDTO.getMessage(), equalTo("Your changes have been successfully saved."));
        assertThat(stateResponseDTO.getData().name, equalTo(payload.getName()));
        assertThat(stateResponseDTO.getData().code, equalTo(payload.getCode()));
        assertThat(stateResponseDTO.getData().enabled, equalTo(payload.getEnabled()));
        assertThat(stateResponseDTO.getData().createdAt, notNullValue());
        assertThat(stateResponseDTO.getData().updatedAt, notNullValue());

        String stateId = stateResponseDTO.getData().id;
        ids.add(stateId);
    }

    private StateDTO getCreateStatePayload() {
        StateDTO stateDTO = new StateDTO();
        stateDTO.setName("e2e");
        stateDTO.setCode(RandomStringUtils.secure().nextAlphabetic(2).toUpperCase());
        stateDTO.setEnabled(true);

        return stateDTO;
    }

    @Step("Clean Up")
    @AfterClass
    public void cleanUp() {
        ids.forEach(stateId -> statesApi.deleteStateById(stateId, Response.Status.OK.getStatusCode()));
    }

}
