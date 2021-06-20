package one.digitalinnovation.beerstock.exception;

public class BeerNotFoundException extends Exception {

    public BeerNotFoundException(String name) {
        super("name " + name + " not found");
    }
}
