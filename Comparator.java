import java.util.ArrayList;
import java.util.HashMap; 
import java.util.Map;
import java.util.Map.Entry;
import java.util.*; 
import java.util.List;
public class Comparator {
	
	void AddedDeleted (HashMap<String, String>h1, HashMap<String, String>h2)
	{
		Iterator iter = h1.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry entry = (Map.Entry)iter.next(); 
			if(h2.get(entry.getKey()) == null)
		       System.out.println("removed attribues: "+entry.getKey()+"="+entry.getValue());
		      
		}
		
		Iterator iter2 = h2.entrySet().iterator();

		while(iter2.hasNext())
		{
		      Map.Entry entry = (Map.Entry)iter2.next();
		      if(h1.get(entry.getKey()) == null)
		          System.out.println("added attributes :"+entry.getKey()+" ="+entry.getValue());
		          
		}
	}
	void updatedPoints(HashMap<String, String>h1, HashMap<String, String>h2)
	{
		Iterator iter = h1.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry entry = (Map.Entry)iter.next(); 
			if(h2.get(entry.getKey())!=null)
			{
			   if(!entry.getValue().equals(h2.get(entry.getKey())))
		       System.out.println(entry.getKey()+"="+entry.getValue()+" is changed to "+entry.getKey()+"="+h2.get(entry.getKey()));
			}
		      
		}
	}
	 void Test(ArrayList<ArrayList<HashMap<String, String>>>list1, ArrayList<ArrayList<HashMap<String, String>>>list2,ArrayList<String>c1,ArrayList<String>c2)
	{
		
//	    System.out.println(Col2);
	    System.out.println("--------------Removed Columns-------------");
	    for(int i=0;i<c1.size();i++)
	    {
	    	if(c2.contains(c1.get(i))==false)
	    	{
	    		System.out.println(c1.get(i)+" " +" column is Removed");
	    	}
	    }
	    System.out.println("--------------Added Columns-------------");
	    for(int i=0;i<c2.size();i++)
	    {
	    	if(c1.contains(c2.get(i))==false)
	    	{
	    		System.out.println(c2.get(i)+" "+" column is Added");
	    	}
	    }
	    
	    System.out.println("--------------Updated Columns-------------");
	    for(int i=0;i<c1.size();i++)
	    {
	    	if(c2.contains(c1.get(i)))           //if column is in list2
	    	{
	    		System.out.println(c1.get(i));
	    		int index1=-1,index2=-1;
	    		for(int j=0;j<list1.size();j++)          //find properties of both the lists for that colm
	    		{
	    			ArrayList<HashMap<String, String>> print = list1.get(j);
	    	        
	    	        	HashMap<String, String> map = print.get(0);         //each list details
	    	        	if(map.containsValue(c1.get(i)))
	    	        	{
	    	        		index1=j;
	    	        		break;
	    	        	}
	    		 }
	    		
	    		for(int j=0;j<list2.size();j++)          //find properties of both the lists for that colm
	    		{
	    			ArrayList<HashMap<String, String>> print = list2.get(j);
	    	        
   	        	HashMap<String, String> map = print.get(0);         //each list details
   	        	if(map.containsValue(c2.get(i)))
   	        	{
   	        		index2=j;
   	        		break;
   	        	}
	    		}
//	    		System.out.println(index1+" "+index2);
	    		//now we have indexes to compare details at them.
	    		ArrayList<HashMap<String, String>> comp1 = list1.get(index1);
	    		ArrayList<HashMap<String, String>> comp2 = list2.get(index2);
	    		System.out.println();
	    		System.out.println(c1.get(i)+" is updated with following changes:");
	    		        
	    			HashMap<String, String> colm1 = comp1.get(0);
   					HashMap<String, String> colm2 = comp2.get(0);
   					System.out.println("changes in column :");
   					AddedDeleted(colm1, colm2);
   					updatedPoints(colm1, colm2);
   					
   			    System.out.println("changes in column objetcs:");
		    		for(int p=1;p<comp1.size();p++)
		    		{
		    			for(int s=1;s<comp2.size();s++)
		    			{
		    				HashMap<String, String> change1 = comp1.get(p);
	   					    HashMap<String, String> change2 = comp2.get(s);
	   					    AddedDeleted(change1, change2);
	   					    updatedPoints(change1, change2);
		   					
		    			}
		    		}
		    		
	    	}
	    }
	    return ;
	}
}

	