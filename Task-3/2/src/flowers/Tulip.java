package flowers;

import java.math.BigDecimal;

public class Tulip extends Flower {
    @Override
    public String getName() {
        return "Тюльпан";
    }

    public Tulip(BigDecimal price) {
        super(price);
    }
}
