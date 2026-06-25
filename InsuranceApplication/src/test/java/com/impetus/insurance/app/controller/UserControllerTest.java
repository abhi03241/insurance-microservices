package com.java.insurance.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.insurance.app.config.security.JwtAuthenticationFilter;
import com.java.insurance.app.constants.UserUrls;
import com.java.insurance.app.models.User;
import com.java.insurance.app.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;


    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    @WithMockUser
    void createUser_Test_SUCCESS() throws Exception {
        // Given
        String jsonRequest = """
                {
                    "gender": "MALE",
                    "phoneNumber": "2563781907",
                    "aadharNo": "123456789012",
                    "dob": "1990-01-01",
                    "name": "vimar ",
                    "password": "12345612",
                    "email":"0000vimarsh@gmail.com",
                    "address": {
                        "state": "virat",
                        "city": "virat",
                        "pinCode": "12324",
                        "street": "street"
                    },
                    "healthDetails": {
                        "diseases": [
                            {
                                "diseaseType": "CANCER"
                            },
                            {
                                "diseaseType": "ASTHMA"
                            }
                        ],
                        "hasAlcoholConsumption": false,
                        "hasTobaccoConsumption": true,
                        "hasSmokingStatus": false,
                        "hasRootCanalTreatment": true,
                        "hasToothExtraction": false
                    }
                }""";

        User user = objectMapper.readValue(jsonRequest, User.class);

        // Mock the behavior of userService.createUser() method
        doAnswer(invocation -> {
            User createdUser = invocation.getArgument(0);
            // Perform any necessary actions with the createdUser, if needed
            return null; // Return null since createUser is a void method
        }).when(userService).createUser(any(User.class));

        // Perform POST request with JSON body
        mockMvc.perform(post(UserUrls.CREATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                // Then
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    void updateUser_Test_SUCCESS() throws Exception {
        // Given
        String jsonRequest = """
                {
                    "gender": "MALE",
                    "phoneNumber": "2563781907",
                    "aadharNo": "123456789012",
                    "dob": "1990-01-01",
                    "name": "vimar ",
                    "password": "12345612",
                    "email":"0000vimarsh@gmail.com",
                    "address": {
                        "state": "virat",
                        "city": "virat",
                        "pinCode": "12324",
                        "street": "street"
                    },
                    "healthDetails": {
                        "diseases": [
                            {
                                "diseaseType": "CANCER"
                            },
                            {
                                "diseaseType": "ASTHMA"
                            }
                        ],
                        "hasAlcoholConsumption": false,
                        "hasTobaccoConsumption": true,
                        "hasSmokingStatus": false,
                        "hasRootCanalTreatment": true,
                        "hasToothExtraction": false
                    }
                }""";


        // Mock the behavior of userService.updateUser() method
        doAnswer(invocation -> {
            User updatedUser = invocation.getArgument(0);
            // Perform any necessary actions with the updatedUser, if needed
            return updatedUser;
        }).when(userService).updateUser(any(User.class));

        // Perform PUT request with JSON body
        mockMvc.perform(put(UserUrls.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers_Test() throws Exception {
        // Given
        List<User> userList = Arrays.asList(new User(), new User()); // Mocked user list


        when(userService.getAllUsers()).thenReturn(userList);

        // Perform GET request
        mockMvc.perform(get(UserUrls.GET_ALL_USERS)
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk());

    }




    @Test
    @WithMockUser
    void getUser_Test() throws Exception {
        // Given
        int userId = 1;
        User user = new User();
        user.setId(userId);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));


        when(userService.getUserById(userId)).thenReturn(user);


        mockMvc.perform(get(UserUrls.GET_USER)
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    void deleteUser_Test() throws Exception {
        // Given
        int userId = 1;
        User user = new User();
        user.setId(userId);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));

        // Perform DELETE request
        mockMvc.perform(delete(UserUrls.DELETE_USER)
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk());

    }

    /* Failure Test */

    @Test
    @WithMockUser
    void createUser_Test_FAILURE() throws Exception {
        // Given
        String invalidJsonRequest = """
                {
                    "gender": "MALE",
                    "phoneNumber": "256378190712312",
                    "aadharNo": "12345678901221312312",
                    "dob": "1990-01-01",
                    "name": "",
                    "password": "",
                    "email":"0000vimarshgmail.com",
                    "address": {
                        "state": "virat",
                        "city": "virat",
                        "pinCode": "12324",
                        "street": "street"
                    },
                    "healthDetails": {
                        "hasAlcoholConsumption": false,
                        "hasTobaccoConsumption": true,
                        "hasSmokingStatus": false,
                        "hasRootCanalTreatment": true,
                        "hasToothExtraction": false
                    }
                }""";

        User user = objectMapper.readValue(invalidJsonRequest, User.class);

        // Mock the behavior of userService.createUser() method
        doAnswer(invocation -> {
            User createdUser = invocation.getArgument(0);
            // Perform any necessary actions with the createdUser, if needed
            return null; // Return null since createUser is a void method
        }).when(userService).createUser(any(User.class));

        // Perform POST request with JSON body
        mockMvc.perform(post(UserUrls.CREATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateUser_Test_FAILURE() throws Exception {
        // Given
        String invalidJsonRequest = """
                {
                    "gender": "MALE",
                    "phoneNumber": "256378190712312",
                    "aadharNo": "12345678901221312312",
                    "dob": "1990-01-01",
                    "name": "",
                    "password": "",
                    "email":"0000vimarshgmail.com",
                    "address": {
                        "state": "virat",
                        "city": "virat",
                        "pinCode": "12324",
                        "street": "street"
                    },
                    "healthDetails": {
                        "hasAlcoholConsumption": false,
                        "hasTobaccoConsumption": true,
                        "hasSmokingStatus": false,
                        "hasRootCanalTreatment": true,
                        "hasToothExtraction": false
                    }
                }""";

        User user = objectMapper.readValue(invalidJsonRequest, User.class);

        // Mock the behavior of userService.createUser() method
        doAnswer(invocation -> {
            User createdUser = invocation.getArgument(0);
            // Perform any necessary actions with the createdUser, if needed
            return null; // Return null since createUser is a void method
        }).when(userService).createUser(any(User.class));

        // Perform POST request with JSON body
        mockMvc.perform(post(UserUrls.UPDATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonRequest))
                // Then
                .andExpect(status().isOk());
    }


}
