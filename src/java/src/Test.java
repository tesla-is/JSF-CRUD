package src;

import java.sql.Connection;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        
        try
        {
            Customer c=new Customer();
            ArrayList al=c.customerList();
            System.out.println(al);
        }
        catch(Exception e)
        {
            
        }
        
    }
    
}
