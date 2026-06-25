package com.java.insurance.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.insurance.app.config.security.JwtService;
import com.java.insurance.app.constants.AuthConstants;
import com.java.insurance.app.models.User;
import com.java.insurance.app.payload.AuthenticationRequest;
import com.java.insurance.app.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private  UserDetailsService userDetailsService;
    @MockBean
    private  UserService userService;
    @MockBean
    private  AuthenticationManager manager;
    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthenticationManager authenticationManager;




    @Test
    @WithMockUser
    void register_Test_SUCCESS() throws Exception {
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
        mockMvc.perform(post(AuthConstants.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void login_Test_SUCCESS() throws Exception {
        // Given
        String email = "0000vimarsh@gmail.com";
        String password = "12345612";

        // Mock the behavior of authenticationManager.authenticate() method
        Authentication authentication = mock(Authentication.class);
        doReturn(authentication).when(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        // Mock the behavior of userService.getUserByEmail() method
        User user = new User();
        user.setEmail(email);
        user.setPassword(password); // Assuming password is encoded
        doReturn(user).when(userService).getUserByEmail(email);

        // Mock the behavior of jwtService.generateToken() method
        String jwtToken = "jwtToken";
        doReturn(jwtToken).when(jwtService).generateToken(user);

        // Perform POST request with JSON body
        mockMvc.perform(post(AuthConstants.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthenticationRequest(email, password))))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void register_Test_FAILURE() throws Exception {
        String invalidJson = """
                {
                    "gender": "m",
                    "phoneNumber": "122563781907",
                    "aadharNo": "12333456789012",
                    "dob": "1990-01-01",
                    "name": "vimar ",
                    "password": "12345612",
                    "email":"0000vimarshgmail.com",
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

        // Mock the behavior of userService.createUser() method to throw an exception
        doThrow(new RuntimeException("Failed to register user")).when(userService).createUser(any(User.class));

        // Perform POST request with JSON body
        mockMvc.perform(post(AuthConstants.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                // Then
                .andExpect(status().isBadRequest()); // Expecting Internal Server Error (500)
    }

    @Test
    @WithMockUser
    public void login_Test_FAILURE() throws Exception {
        // Given
        String email = "nonexistent@example.com";
        String password = "anyPassword";

        doThrow(new BadCredentialsException("Bad credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(post(AuthConstants.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthenticationRequest(email, password))))
                // Then
                .andExpect(status().isOk());

    }
}
