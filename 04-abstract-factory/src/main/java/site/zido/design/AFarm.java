package site.zido.design;

public class AFarm implements Farm {
    @Override
    public Animal newAnimal() {
        return new Cattle();
    }

    @Override
    public Plant newPlant() {
        return new Vegetables();
    }
}
