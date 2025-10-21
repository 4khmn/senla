public class Car implements IProduct{

    private IProductPart body;
    private IProductPart chassis;
    private IProductPart engine;
    @Override
    public void installFirstPart(IProductPart part) {
        System.out.println("Устанавливается кузов в автомобиль");
        this.body = part;

    }

    @Override
    public void installSecondPart(IProductPart part) {
        System.out.println("Устанавливаются шасси в автомобиль");
        this.chassis = part;

    }

    @Override
    public void installThirdPart(IProductPart part) {
        System.out.println("Устанавливается двигатель в автомобиль");
        this.engine=part;
    }

    @Override
    public String toString() {
        return "Автомобиль собран: кузов + шасси + двигатель";
    }
}
