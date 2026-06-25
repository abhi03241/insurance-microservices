package com.java.insurance.app.controller;


import com.java.insurance.app.constants.AdminUrls;
import com.java.insurance.app.models.User;
import com.java.insurance.app.repositories.UserRepository;
import com.java.insurance.app.services.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private UserRepository userRepository;


    @Test
    @WithMockUser(roles = "ADMIN")
    void createAdmin_Test_SUCCESS() throws Exception {

        String json = "{\"gender\":\"MALE\"," + "\"phoneNumber\":\"9109112547\"," + "\"aadharNo\":\"123456789066\"," + "\"dob\":\"1990-01-01\"," + "\"name\":\"Underwriter Name\"," + "\"password\":\"password123\"," + "\"email\":\"underwriter@example.com\"," + "\"address\":{\"state\":\"State\",\"city\":\"City\",\"pinCode\":\"12345\",\"street\":\"Street\"}," + "\"healthDetails\":{\"diseases\":[],\"hasAlcoholConsumption\":false,\"hasTobaccoConsumption\":true,\"hasSmokingStatus\":false,\"hasRootCanalTreatment\":true,\"hasToothExtraction\":false}}";

        mockMvc.perform(post(AdminUrls.CREATE_ADMIN).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUnderwriter_Test_SUCCESS() throws Exception {
        String json = "{\"gender\":\"MALE\"," + "\"phoneNumber\":\"9109112547\"," + "\"aadharNo\":\"123456789066\"," + "\"dob\":\"1990-01-01\"," + "\"name\":\"Underwriter Name\"," + "\"password\":\"password123\"," + "\"email\":\"underwriter@example.com\"," + "\"address\":{\"state\":\"State\",\"city\":\"City\",\"pinCode\":\"12345\",\"street\":\"Street\"}," + "\"healthDetails\":{\"diseases\":[],\"hasAlcoholConsumption\":false,\"hasTobaccoConsumption\":true,\"hasSmokingStatus\":false,\"hasRootCanalTreatment\":true,\"hasToothExtraction\":false}}";

        mockMvc.perform(post(AdminUrls.CREATE_UNDERWRITER).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAdmin_Test_SUCCESS() throws Exception {
        int adminId = 1;
        when(userRepository.findById(adminId)).thenReturn(Optional.ofNullable(User.builder().id(adminId).build()));
        mockMvc.perform(delete(AdminUrls.DELETE_ADMIN, adminId)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUnderwriter_Test_SUCCESS() throws Exception {
        int underwriterId = 1;
        when(userRepository.findById(underwriterId)).thenReturn(Optional.ofNullable(User.builder().id(underwriterId).build()));
        mockMvc.perform(delete(AdminUrls.DELETE_UNDERWRITER, underwriterId)).andExpect(status().isOk());
    }

    /* Failure Tests */
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createAdmin_UserRole_Failure() throws Exception {

        String json = "{\"gender\":\"MALE\"," + "\"phoneNumber\":\"9109112547\"," + "\"aadharNo\":\"123456789066\"," + "\"dob\":\"1990-01-01\"," + "\"name\":\"Underwriter Name\"," + "\"password\":\"password123\"," + "\"email\":\"underwriter@example.com\"," + "\"address\":{\"state\":\"State\",\"city\":\"City\",\"pinCode\":\"12345\",\"street\":\"Street\"}," + "\"healthDetails\":{\"diseases\":[],\"hasAlcoholConsumption\":false,\"hasTobaccoConsumption\":true,\"hasSmokingStatus\":false,\"hasRootCanalTreatment\":true,\"hasToothExtraction\":false}}";

        mockMvc.perform(MockMvcRequestBuilders.post(AdminUrls.CREATE_ADMIN).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "USER")
    void deleteAdminWithoutPermission_Test_FAILURE() throws Exception {
        int adminId = 1;
        mockMvc.perform(delete(AdminUrls.DELETE_ADMIN, adminId)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createUnderwriter_UserRole_Failure() throws Exception {
        // JSON representation of the user object for underwriter
        String json = "{\"gender\":\"MALE\"," + "\"phoneNumber\":\"9109112547\"," + "\"aadharNo\":\"123456789066\"," + "\"dob\":\"1990-01-01\"," + "\"name\":\"Underwriter Name\"," + "\"password\":\"password123\"," + "\"email\":\"underwriter@example.com\"," + "\"address\":{\"state\":\"State\",\"city\":\"City\",\"pinCode\":\"12345\",\"street\":\"Street\"}," + "\"healthDetails\":{\"diseases\":[],\"hasAlcoholConsumption\":false,\"hasTobaccoConsumption\":true,\"hasSmokingStatus\":false,\"hasRootCanalTreatment\":true,\"hasToothExtraction\":false}}";

        mockMvc.perform(MockMvcRequestBuilders.post(AdminUrls.CREATE_UNDERWRITER).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteUnderwriterWithoutPermission_Test_FAILURE() throws Exception {
        int underwriterId = 1;
        mockMvc.perform(delete(AdminUrls.DELETE_UNDERWRITER, underwriterId)).andExpect(status().isForbidden());
    }


}
