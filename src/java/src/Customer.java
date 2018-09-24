package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class Customer {

    int id;
    String name;
    String email;
    ArrayList customerList;
    Connection con;
    Map<String,Object> sessionMap=FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList getCustomerList() {
        return customerList;
    }

    public void setCustomerList(ArrayList customerList) {
        this.customerList = customerList;
    }
    
    public Connection getConnection()
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
        }
        catch(Exception e)
        {
            System.out.println("Connection not established");
        }
        
        return con;
    }
    
    public ArrayList customerList()
    {
        customerList=new ArrayList();
        try
        {
            con=getConnection();
            PreparedStatement stmt=con.prepareStatement("select * from customers");
            ResultSet rs=stmt.executeQuery();
            while(rs.next())
            {
                Customer customer=new Customer();
                customer.setId(rs.getInt(1));
                customer.setName(rs.getString(2));
                customer.setEmail(rs.getString(3));
                customerList.add(customer);
            }
        }
        catch(Exception e)
        {
            
        }
        
        return customerList;
    }
    
    public String save()
    {
        int result=0;
        
        try
        {
            con=getConnection();
            PreparedStatement stmt=con.prepareStatement("insert into customers values(?,?,?)");
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, email);
            result=stmt.executeUpdate();
        }
        catch(Exception e)
        {
            
        }
        
        if(result!=0)
        {
            return "index.xhtml?faces-redirect=true";
        }
        else
        {
            return "create.xhtml?faces-redirect=true";
        }
    }
    
    public void delete(int id)
    {
        try
        {
            con=getConnection();
            PreparedStatement stmt=con.prepareStatement("delete from customers where id=?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            
        }
    }
    
    public String edit(int id)
    {
        Customer customer=null;
        try
        {
            con=getConnection();
            PreparedStatement stmt=con.prepareStatement("select * from customers where id=?");
            stmt.setInt(1, id);
            ResultSet rs=stmt.executeQuery();
            rs.next();
            customer=new Customer();
            customer.setId(rs.getInt(1));
            customer.setName(rs.getString(2));
            customer.setEmail(rs.getString(3));
            sessionMap.put("editCustomer", customer);
            
        }
        catch(Exception e)
        {
            
        }
        
        return "/edit.xhtml?faces-redirect=true";
    }
    
    public String update(Customer customer)
    {
        try
        {
            con=getConnection();
            PreparedStatement stmt=con.prepareStatement("update customers set name=?, email=? where id=?");
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setInt(3, customer.getId());
            stmt.executeQuery();
        }
        catch(Exception e)
        {
            
        }
        
        return "/index.xhtml?faces-redirect=true";
    }
}
