package steps;

import parts.Engine;
import parts.IProductPart;

public class EngineStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Строится двигатель");
        return new Engine();
    }
}
