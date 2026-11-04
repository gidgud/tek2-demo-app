package dk.ek.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Lille REST-API til eksamensdemoer:
 *  - GET vs POST            -> /api/notes
 *  - statuskoder 2xx-5xx    -> /api/status/{code}
 *  - Accept / Content-Type  -> POST /api/notes med JSON
 *  - sessions/cookies       -> /login og /me
 */
@RestController
public class NoteController {

    public record Note(int id, String text) {}

    private final List<Note> notes = new CopyOnWriteArrayList<>();

    @GetMapping("/")
    public Map<String, String> hello() {
        return Map.of("message", "Hej fra TEK2 demo-app", "version", "1.0.0");
    }

    @GetMapping("/api/notes")
    public List<Note> list() {
        return notes;
    }

    @PostMapping("/api/notes")
    public ResponseEntity<Note> create(@RequestBody Map<String, String> body) {
        String text = body.get("text");
        if (text == null || text.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Note note = new Note(notes.size() + 1, text);
        notes.add(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    @GetMapping("/api/status/{code}")
    public ResponseEntity<Map<String, Integer>> status(@PathVariable int code) {
        return ResponseEntity.status(code).body(Map.of("status", code));
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String user, HttpSession session) {
        session.setAttribute("user", user);
        return Map.of("loggedIn", user);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "ikke logget ind"));
        }
        return ResponseEntity.ok(Map.of("user", user.toString()));
    }
}
