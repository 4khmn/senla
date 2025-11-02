package flowers;

import java.math.BigDecimal;
public class Orchid extends Flower {
    public Orchid(BigDecimal price) {
        super(price);
    }

    @Override
    public String getName() {
        return "Орхидея";
    }
}
