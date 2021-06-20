package one.digitalinnovation.beerstock.exception;

public class BeerNotFoundIdException extends Exception {

    public BeerNotFoundIdException(Long id) {
        super("ID " + id + " Not Found");
    }
}
