package com.store.gamestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    public void testAllGames() throws Exception{
        List<Game> games = Arrays.asList(new Game(1L, "Game 1", "Description of Game 1", 15.99),
                                         new Game(2L, "Game 2", "Description of Game 2", 24.15));
        Mockito.when(gameService.getAllGames()).thenReturn(games);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/games")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(2)))
                .andExpect((ResultMatcher) jsonPath("$[0].name", is("Game 1")))
                .andExpect((ResultMatcher) jsonPath("$[1].name", is("Game 2")));
    }

    @Test
    public void testAddGame() throws Exception{
        Game game = new Game(null, "New Game", "A new game for testing", 59.99);
        Mockito.when(gameService.addGame(Mockito.any(Game.class))).thenReturn(game);

        String gameJson = "{\"name\": \"New Game\", \"description\": \"A new game for testing\", \"price\": 59.99}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/games")
                .content(gameJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/games/0"))
                .andExpect((ResultMatcher) jsonPath("$.name", is("New Game")))
                .andExpect((ResultMatcher) jsonPath("$.description", is("A new game for testing")))
                .andExpect((ResultMatcher) jsonPath("$.price", is(59.99)));
    }

    @Test
    public void testUpdateGame() throws Exception {
        Game game = new Game(1L, "Updated Game", "An updated game for testing", 69.99);
        Mockito.when(gameService.updateGame(Mockito.eq(1L), Mockito.any(Game.class))).thenReturn(game);

        String gameJson = "{\"name\": \"Updated Game\", \"description\": \"An updated game for testing\", \"price\": 69.99}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/games/1")
                        .content(gameJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name", is("Updated Game")))
                .andExpect((ResultMatcher) jsonPath("$.description", is("An updated game for testing")))
                .andExpect((ResultMatcher) jsonPath("$.platform", is("PS5")))
                .andExpect((ResultMatcher) jsonPath("$.price", is(69.99)));
    }

    @Test
    public void testDeleteGame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/games/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(gameService, times(1)).deleteGame(Mockito.eq(1L));
    }
}
