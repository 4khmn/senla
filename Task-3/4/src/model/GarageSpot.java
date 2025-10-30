package model;

public class GarageSpot {
    private static int global_id=1; // for serial primary key
    private final int id;
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

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public int getId() {
        return id;
    }

    public GarageSpot() {
        this.id = global_id++;
    }
}
