package com.store.gamestore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public List<Game> getAllGames(){
        return gameService.getAllGames();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Game>> searchGames(@RequestParam(name = "name") String name){
        List<Game> foundGames = gameService.searchByName(name);
        return ResponseEntity.ok(foundGames);
    }

    @PostMapping
    public ResponseEntity<Game> addGame(@RequestBody Game game){
        Game newGame = gameService.addGame(game);
        return ResponseEntity.created(URI.create("/api/games" + newGame.getId()))
                .body(newGame);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game game){
        Game updatedGame = gameService.updateGame(id, game);
        return ResponseEntity.ok(updatedGame);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id){
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

}
