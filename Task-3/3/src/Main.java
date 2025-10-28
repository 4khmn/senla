import steps.BodyStep;
import steps.ChassisStep;
import steps.EngineStep;
import steps.ILineStep;

public class Main {
    public static void main(String[] args) {
        ILineStep bodyStep = new BodyStep();
        ILineStep chassisStep = new ChassisStep();
        ILineStep engineStep = new EngineStep();

        IAssemblyLine assemblyLine = new CarAssemblyLine(bodyStep, chassisStep, engineStep);

        IProduct car = new Car();

        car = assemblyLine.assembleProduct(car);

        System.out.println(car);
    }
}
