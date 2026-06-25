package com.java.insurance.app.controller;

import com.java.insurance.app.constants.RoleUrls;
import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.services.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoleControllerTest {

    @MockBean
    private RoleService roleService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateRole() throws Exception {
        String roleJson = "{\"roleType\":\"CUSTOMER\"}";

        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.ADMIN);

        when(roleService.createRole(Mockito.any(Role.class))).thenReturn(role);

        mockMvc.perform(MockMvcRequestBuilders.post(RoleUrls.CREATE_ROLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleJson))
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getRole() throws Exception {
        int roleId = 1;
        Role role = new Role();
        role.setId(roleId);
        role.setRoleType(RoleType.CUSTOMER);

        when(roleService.getRole(roleId)).thenReturn(role);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get(RoleUrls.GET_ROLE, roleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(roleId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleType").value("CUSTOMER"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllRoles() throws Exception {
        // Given
        Role role1 = new Role();
        role1.setId(1);
        role1.setRoleType(RoleType.ADMIN);

        Role role2 = new Role();
        role2.setId(2);
        role2.setRoleType(RoleType.CUSTOMER);

        List<Role> roles = Arrays.asList(role1, role2);

        when(roleService.getAllRoles()).thenReturn(roles);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get(RoleUrls.GET_ALL_ROLE)
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roleType").value("ADMIN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].roleType").value("CUSTOMER"));
    }
}
