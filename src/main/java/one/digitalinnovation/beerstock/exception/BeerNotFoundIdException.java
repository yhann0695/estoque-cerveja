package one.digitalinnovation.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BeerNotFoundIdException extends Exception {

    public BeerNotFoundIdException(Long id) {
        super("ID " + id + " Not Found");
    }
}
