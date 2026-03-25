package com.virtuallink.virtualbank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VirtualbankApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Crée un utilisateur et retourne son id. */
    private String createUser(String firstName, String lastName, String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "%s",
                                  "lastName": "%s",
                                  "email": "%s",
                                  "password": "secret123"
                                }
                                """.formatted(firstName, lastName, email)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    /** Crée un compte pour un userId et retourne son id. */
    private String createAccount(String userId, double initialBalance) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "%s",
                                  "initialBalance": %s
                                }
                                """.formatted(userId, initialBalance)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Test
    void closedAt_shouldBeHiddenWhenAccountIsOpen_andVisibleAfterClose() throws Exception {
        // 1) Créer un utilisateur
        String userId = createUser("Jean", "Dupont", "jean.dupont@test.com");

        // 2) Créer un compte - closedAt doit être absent
        String accountId = createAccount(userId, 100.0);

        mockMvc.perform(get("/api/v1/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.closedAt").doesNotExist())
                .andExpect(jsonPath("$.userId").value(userId));

        // 3) Fermer le compte - closedAt doit apparaître
        mockMvc.perform(delete("/api/v1/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountStatus").value("CLOSED"))
                .andExpect(jsonPath("$.closedAt").isString());

        // 4) Relire le compte - contrat JSON persiste
        mockMvc.perform(get("/api/v1/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountStatus").value("CLOSED"))
                .andExpect(jsonPath("$.closedAt").isString());
    }

    @Test
    void createBatch_shouldCreateMultipleAccounts_andHideClosedAtWhenNull() throws Exception {
        // Créer deux utilisateurs
        String userId1 = createUser("Marie", "Martin", "marie.martin@test.com");
        String userId2 = createUser("Paul", "Bernard", "paul.bernard@test.com");

        mockMvc.perform(post("/api/v1/accounts/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accounts": [
                                    { "userId": "%s", "initialBalance": 50.0 },
                                    { "userId": "%s", "initialBalance": 0.0 }
                                  ]
                                }
                                """.formatted(userId1, userId2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].ownerName").value("Marie Martin"))
                .andExpect(jsonPath("$[1].ownerName").value("Paul Bernard"))
                .andExpect(jsonPath("$[0].closedAt").doesNotExist())
                .andExpect(jsonPath("$[1].closedAt").doesNotExist());
    }

    @Test
    void user_shouldCreateUserAndListAccounts() throws Exception {
        // 1) Créer un utilisateur
        String userId = createUser("Alice", "Leroy", "alice.leroy@test.com");
        assertThat(userId).isNotBlank();

        // 2) Créer deux comptes pour cet utilisateur
        createAccount(userId, 1000.0);
        createAccount(userId, 500.0);

        // 3) Lister les comptes de l'utilisateur
        mockMvc.perform(get("/api/v1/users/{id}/accounts", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[1].userId").value(userId));
    }

    @Test
    void user_createDuplicateEmail_shouldReturn409() throws Exception {
        createUser("Bob", "Dupont", "bob.dupont@test.com");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Robert",
                                  "lastName": "Dupont",
                                  "email": "bob.dupont@test.com",
                                  "password": "secret123"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}
