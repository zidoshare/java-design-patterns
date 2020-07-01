package site.zido.design;

public class BFarm implements Farm {
    @Override
    public Animal newAnimal() {
        return new Horse();
    }

    @Override
    public Plant newPlant() {
        return new Fruit();
    }
}
