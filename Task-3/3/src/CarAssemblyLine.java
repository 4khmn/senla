public class CarAssemblyLine implements IAssemblyLine{

    private ILineStep bodyStep;
    private ILineStep chassisStep;
    private ILineStep engineStep;


    public CarAssemblyLine(ILineStep body, ILineStep chassis, ILineStep engine) {
        this.bodyStep = body;
        this.chassisStep = chassis;
        this.engineStep = engine;
    }



    @Override
    public IProduct assembleProduct(IProduct product) {
        System.out.println("Начало сборки автомобиля");
        IProductPart body = bodyStep.buildProductPart();
        product.installFirstPart(body);

        IProductPart chassis = chassisStep.buildProductPart();
        product.installSecondPart(chassis);

        IProductPart engine = engineStep.buildProductPart();
        product.installThirdPart(engine);
        System.out.println("Конец сборки автомобиля");
        return product;
    }
}
