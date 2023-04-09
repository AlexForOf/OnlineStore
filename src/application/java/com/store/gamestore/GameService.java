package com.store.gamestore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Transactional(readOnly = true)
    public List<Game> getAllGames(){
        return gameRepository.findAll();
    }

    public List<Game> searchByName(String name){
        return gameRepository.findByName(name);
    }

    @Transactional
    public Game addGame(Game game){
        return gameRepository.save(game);
    }

    @Transactional
    public Game updateGame(Long id, Game game){
        Game foundGame = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));

        foundGame.setName(game.setName(game.getName()));
        foundGame.setDescription(game.getDescription());
        foundGame.setPrice(game.getPrice());

        return gameRepository.save(foundGame);
    }

    @Transactional
    public void deleteGame(Long id){
        Game foundGame = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));

        gameRepository.delete(foundGame);
    }
}
