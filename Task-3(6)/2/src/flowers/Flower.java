package flowers;

import java.math.BigDecimal;

public abstract class Flower {
    private BigDecimal price;
    public BigDecimal getPrice() {
        return price;
    }
    public abstract String getName();

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Flower(BigDecimal price) {
        this.price = price;
    }

}
