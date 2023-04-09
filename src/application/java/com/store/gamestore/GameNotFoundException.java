package com.store.gamestore;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long id){
        super("Game with following id wasn't found: " + id);
    }
}
