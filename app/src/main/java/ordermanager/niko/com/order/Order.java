package ordermanager.niko.com.order;

import java.util.Date;

/*
Created 17.04.2019
Neobhodimo dobaviti foto
 */
public class Order {
    public String Name;
    public Date StartTime;
    public Date NeedEndTime;
    public String Description;
    public int Cost;

    public Order(){
        Name="Order";
        StartTime= new Date();
        Description="";
        Cost=0;
    }
}
