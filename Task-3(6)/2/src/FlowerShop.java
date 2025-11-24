import flowers.Chrysanthemum;
import flowers.Orchid;
import flowers.Rose;
import flowers.Tulip;

import java.math.BigDecimal;

public class FlowerShop {
    public static void main(String[] args) {
        Bouquet bouquet = new Bouquet();
        Rose rose = new Rose(new BigDecimal(150.0));
        Orchid orchid = new Orchid(new BigDecimal(550.0));
        Tulip tulip = new Tulip(new BigDecimal(70.0));
        Chrysanthemum chrysanthemum = new Chrysanthemum(new BigDecimal(200.0));
        bouquet.addFlower(rose);
        bouquet.addFlower(rose);
        bouquet.addFlower(orchid);
        bouquet.addFlower(tulip);
        bouquet.addFlower(chrysanthemum);
        bouquet.addFlower(chrysanthemum);
        bouquet.addFlower(chrysanthemum);
        System.out.println(bouquet.getBouquete());

    }
}
