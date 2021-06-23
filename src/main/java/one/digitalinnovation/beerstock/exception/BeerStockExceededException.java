package one.digitalinnovation.beerstock.exception;

public class BeerStockExceededException extends Exception {

    public BeerStockExceededException(Long id, int quantityToIncrement) {
        super("The quantity '" + quantityToIncrement + "' exceeds the maximum value of the beer with the ID '" + id + "'");
    }
}
