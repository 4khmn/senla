public class GarageSpot {
    private static int glonal_id=1; // for serial primary key
    private int id;
    private boolean isFree = true;

    public void occupy(){
        if (this.isFree==true) {
            this.isFree = false;
        }
        else{
            System.out.println("This parking lot is already taken");
        }
    }
    public void freeUp(){
        if (this.isFree==false) {
            this.isFree = true;
        }
        else{
            System.out.println("This parking lot is already free!");
        }
    }

    public GarageSpot() {
        this.id = glonal_id++;
    }
}
