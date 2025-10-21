public class ChassisStep implements ILineStep{
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Строится шасси");
        return new Chassis();
    }
}
