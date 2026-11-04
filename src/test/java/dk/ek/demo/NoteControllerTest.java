package dk.ek.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void helloReturnsMessage() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk())
           .andExpect(jsonPath("$.message").value("Hej fra TEK2 demo-app"));
    }

    @Test
    void postCreatesNote() throws Exception {
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"eksamen\"}"))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.text").value("eksamen"));
    }

    @Test
    void emptyNoteIsBadRequest() throws Exception {
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content("{}"))
           .andExpect(status().isBadRequest());
    }
}
