package one.digitalinnovation.beerstock.exception;

public class BeerAlreadyRegisteredException extends Exception {

    public BeerAlreadyRegisteredException(String nome) {
        super("Name " + nome + " is registred");
    }
}
