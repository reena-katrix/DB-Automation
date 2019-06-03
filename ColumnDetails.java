import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.io.InputStreamReader;


 class ColumnDetails {

	String name;
	String type;
	ArrayList<ColumnObjects> obj= new ArrayList<ColumnObjects>();
	ColumnDetails()               //default constructor
	{
		  this.name = " ";
		  this.type = " ";
		  
	}
	// to add column attributes
	ColumnDetails(ArrayList<HashMap<String, String>>result)
	{
		//to process parent i.e.column
		HashMap<String, String>parent = result.get(0);
		for(Entry<String, String> m:parent.entrySet()){ 
			String key = m.getKey();
			switch(key){
			case "name" :
	        	 if(m.getValue()!=null)
	        		 this.name = m.getValue();
	        	 break;
	          case "type" :
	        	  if(m.getValue()!=null)
		             this.type = m.getValue();
	        	  break;
	          default :
	              break;
	       }
	   	}
		
		//to process childs
		for(int i=1;i<result.size();i++)
		{
			if(!result.get(i).isEmpty()){
			obj.add(new ColumnObjects(result.get(i)));
			}
		}
	}
	void printColumn()
	{
		System.out.println("Column Details:");
		System.out.println("Column name"+" "+"="+" "+this.name + " " +"type"+" "+"="+" "+ this.type);
		if(obj.size()>0){
			System.out.println("Column objects Info:");
			for(int i=0;i<obj.size();i++)
				obj.get(i).printColumnObjectAttriInfo();
		}
	}
	
	String getName()
	{
		return this.name;
	}
	String getType()
	{
		return this.type;
	}
	ArrayList<ColumnObjects> getColObj()
	{
		return obj;
	} 
}
 
