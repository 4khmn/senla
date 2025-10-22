package flowers;

import java.math.BigDecimal;
public class Rose extends Flower {

    @Override
    public String getName() {
        return "Роза";
    }

    public Rose(BigDecimal price) {
        super(price);
    }

}
