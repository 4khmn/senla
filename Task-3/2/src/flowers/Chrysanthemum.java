package flowers;

import java.math.BigDecimal;

public class Chrysanthemum extends Flower {
    public Chrysanthemum(BigDecimal price) {
        super(price);
    }

    @Override
    public String getName() {
        return "Хризантема";
    }
}
