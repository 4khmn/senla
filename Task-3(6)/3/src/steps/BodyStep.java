package steps;

import parts.Body;
import parts.IProductPart;

public class BodyStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Строится кузов");
        return new Body();
    }
}
