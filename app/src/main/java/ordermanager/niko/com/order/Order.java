package ordermanager.niko.com.order;

import java.util.Date;
import java.util.UUID;

/*
Created 17.04.2019
Neobhodimo dobaviti foto
 */
public class Order {

    private UUID uuid;
    public String Name;
    public Date StartTime;
    public Date NeedEndTime;
    public String Description;
    public int Cost;

    public UUID getUuid() {
        return uuid;
    }

    public Order(){
        uuid= UUID.randomUUID();
        Name="Order";
        StartTime= new Date();
        Description="";
        Cost=0;
    }
}
