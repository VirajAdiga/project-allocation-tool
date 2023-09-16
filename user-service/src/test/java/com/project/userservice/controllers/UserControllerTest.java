    package com.project.userservice.controllers;

    import com.project.userservice.mappers.UserMapper;
    import com.project.userservice.entities.DBUser;
    import com.project.userservice.dto.PublicUser;
    import com.project.userservice.entities.enums.Role;
    import com.project.userservice.services.UserService;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;
    import org.springframework.dao.DataIntegrityViolationException;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageImpl;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.http.MediaType;
    import org.springframework.test.web.servlet.MockMvc;
    import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
    import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
    import org.springframework.test.web.servlet.setup.MockMvcBuilders;
    import com.fasterxml.jackson.databind.ObjectMapper;


    import java.util.Arrays;
    import java.util.List;
    import static org.mockito.ArgumentMatchers.anyInt;
    import static org.mockito.Mockito.*;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    public class UserControllerTest {
        private MockMvc mockMvc;
        @Mock
        private UserService userService;
        @Mock
        private UserMapper userMapper;
        private ObjectMapper objectMapper;


        @BeforeEach
        public void setup() {
            MockitoAnnotations.openMocks(this);
            objectMapper = new ObjectMapper();

            // Mocking getAllUsers with pagination
            List<DBUser> dbUsersWithPagination = Arrays.asList(
                    new DBUser(1, "User1", "user1@example.com", "password", Role.EMPLOYEE, null, false, null),
                    new DBUser(2, "User2", "user2@example.com", "password", Role.EMPLOYEE, null, false, null)
            );
            Page<DBUser> userPageWithPagination = new PageImpl<>(dbUsersWithPagination, PageRequest.of(0, 10), dbUsersWithPagination.size());

            when(userService.getAllUsers(anyInt(), anyInt())).thenReturn(userPageWithPagination);

            List<PublicUser> publicUsersWithPagination = Arrays.asList(
                    new PublicUser(1, "User1", "user1@example.com", Role.EMPLOYEE, false, null),
                    new PublicUser(2, "User2", "user2@example.com", Role.EMPLOYEE, false, null)
            );
            when(userMapper.entityToPublicModel(userPageWithPagination.getContent())).thenReturn(publicUsersWithPagination);

            // Mocking getAllUsersWithoutPagination
            List<DBUser> dbUsersWithoutPagination = Arrays.asList(
                    new DBUser(1, "User1", "user1@example.com", "password", Role.EMPLOYEE, null, false, null),
                    new DBUser(2, "User2", "user2@example.com", "password", Role.EMPLOYEE, null, false, null)
            );
            when(userService.getAllInterviewers()).thenReturn(dbUsersWithoutPagination);

            List<PublicUser> publicUsersWithoutPagination = Arrays.asList(
                    new PublicUser(1, "User1", "user1@example.com", Role.EMPLOYEE, false, null),
                    new PublicUser(2, "User2", "user2@example.com", Role.EMPLOYEE, false, null)
            );
            when(userMapper.entityToPublicModel(dbUsersWithoutPagination)).thenReturn(publicUsersWithoutPagination);

            UserController userController = new UserController(userService, userMapper);
            mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        }


        @Test
        public void testGetAllUsers() throws Exception {
            mockMvc.perform(get("/api/v1/users")
                            .param("pageSize", "5")
                            .param("pageNumber", "0"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.users").isArray())
                    .andExpect(jsonPath("$.users[0].id").value(1))
                    .andExpect(jsonPath("$.users[0].name").value("User1"))
                    .andExpect(jsonPath("$.users[1].id").value(2))
                    .andExpect(jsonPath("$.users[1].name").value("User2"))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andDo(MockMvcResultHandlers.print());
        }

        @Test
        public void testGetAllUsersWithoutPagination() throws Exception {
            mockMvc.perform(get("/api/v1/users/interviewers"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].name").value("User1"))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].name").value("User2"))
                    .andDo(MockMvcResultHandlers.print());
        }

        @Test
        public void testDeleteUser_Success() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{userId}", 1))
                    .andExpect(status().isOk())
                    .andExpect(content().string("User deleted successfully"))
                    .andDo(MockMvcResultHandlers.print());
        }

        @Test
        public void testDeleteUser_Failure() throws Exception {
            // Mock UserService's deleteUser method to throw DataIntegrityViolationException
            doThrow(DataIntegrityViolationException.class).when(userService).deleteUser(1);

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{userId}", 1))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Cannot delete this user. This user is scheduled to conduct interviews."))
                    .andDo(MockMvcResultHandlers.print());
        }

        @Test
        public void testPartialUpdateAdminUser_Success() throws Exception {
            // Prepare the input data
            PublicUser updatedUser = new PublicUser();
            updatedUser.setName("UpdatedName");
            updatedUser.setEmail("updated@example.com");
            updatedUser.setRole(Role.ADMIN);

            // Perform the request and assert the response
            String requestBody = objectMapper.writeValueAsString(updatedUser);

            mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/1/admin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk());
        }

        @Test
        public void testPartialUpdateAdminUser_Failure() throws Exception {
            // Mock UserService's partialUpdateAdminUser method to throw a RuntimeException
            doThrow(new RuntimeException("Test exception")).when(userService).partialUpdateAdminUser(any(Integer.class), any(PublicUser.class));

            PublicUser updatedUser = new PublicUser();
            updatedUser.setId(1);
            updatedUser.setName("UpdatedName");
            updatedUser.setEmail("updated@example.com");
            updatedUser.setRole(Role.ADMIN);

            mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{userId}/admin", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedUser)))
                    .andExpect(status().isInternalServerError());
        }

    }
