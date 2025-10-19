import flowers.Flower;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bouquet {
    private Map<Flower, Integer> bouquete = new LinkedHashMap();
    private BigDecimal amount = BigDecimal.ZERO;
    public void addFlower(Flower flower){
        if (!bouquete.containsKey(flower)){
            bouquete.put(flower, 1);
        }
        else {
            bouquete.put(flower, bouquete.get(flower) + 1);
        }
        amount = amount.add(flower.getPrice());
    }




    public String getBouquete() {
        String message = "Ваш букет: ";
        for (var v: bouquete.entrySet()){
            message+="\n" + v.getKey().getName() + " - " + v.getValue() + " (шт)";
        }

        message +="\n" + "Итоговая стоимость: " + amount;
        return message;
    }
}
