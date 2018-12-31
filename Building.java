package assignment3;

public class Building {

	OneBuilding data;
	Building older;
	Building same;
	Building younger;
	
	public Building(OneBuilding data){
		this.data = data;
		this.older = null;
		this.same = null;
		this.younger = null;
	}
	
	public String toString(){
		String result = this.data.toString() + "\n";
		if (this.older != null){
			result += "older than " + this.data.toString() + " :\n";
			result += this.older.toString();
		}
		if (this.same != null){
			result += "same age as " + this.data.toString() + " :\n";
			result += this.same.toString();
		}
		if (this.younger != null){
			result += "younger than " + this.data.toString() + " :\n";
			result += this.younger.toString();
		}
		return result;
	}
	
	public Building addBuilding (OneBuilding b){
		
		Building obToAdd = new Building (b);
		
		//if building to add is same age
		if (b.yearOfConstruction == this.data.yearOfConstruction)
		{
			//compare heights
			if (b.height>this.data.height)
			{
				//b has to take the this info
				obToAdd.older = this.older;
				obToAdd.younger = this.younger;
				//reset info of the this
				this.older = null;
				this.younger = null;
				//add it to the new root
				obToAdd.same = this;
				//this is now the new root so return it to make it the this
				return obToAdd;
			}else //guarantees that the b is just to be added to the root
			{
				//if there is something 
				if (this.same != null)
				{
					//this will handle height hierarchy 
					this.same = this.same.addBuilding(b);
					//that spot now responsible as ref to add
				}else //means it is null and you can just add
				
					this.same = obToAdd;
	
			}
		}
		
		//older
		else if (b.yearOfConstruction < this.data.yearOfConstruction)
		{
			//if there is an older
			if (this.older != null)
			{
				//do addbuilding with that as ref now
				this.older = this.older.addBuilding(b);
			} else
			{
				//only change the appropriate this older
				this.older = obToAdd;
				//obToAdd has null pointers for future branches of its own
			}
		}
		
		//younger
		else if (b.yearOfConstruction > this.data.yearOfConstruction)
		{
			if (this.younger != null)
			{
				this.younger = this.younger.addBuilding(b);
			}
			else 
			{
				this.younger = obToAdd;
			}
		}
		return this; 
	}
	
	public Building addBuildings (Building b){
		
		//all recursion through all leaves in branch is
		//handled in the addBuilding
		//add data
		this.addBuilding(b.data);
		
		//older
		if (b.older != null)
		{
			this.addBuildings(b.older);	
		}
		//same
		if (b.same != null)
		{
			this.addBuildings(b.same);
		}
		//younger
		if (b.younger != null)
		{
			this.addBuildings(b.younger);
		}
		//will return the root end at
		return this; 
	}
	
	public Building removeBuilding (OneBuilding b){
		
		//if the node to remove is found 
		if (this.data.equals(b))
		{
			//if theres a same under the removed, we make it new root
			if (this.same != null)
			{
				//adjust older
				if (this.older != null)
				{
					this.same = (this.same).addBuildings(this.older);
				}
				//adjust younger
				if (this.younger != null)
				{
					this.same = (this.same).addBuildings(this.younger);
				}
				return this.same;
			}
			//otherwise, older becomes new root (same is empty)
			else if (this.older != null)
			{
				//only the younger branch to add to it
				this.older = (this.older).addBuildings(this.younger);
				return this.older;
			}
			//otherwise younger becomes the root (same and older are empty)
			else if (this.younger != null)
			{
				return this.younger;
			}
			//otherwise the entire thing is empty now
			else 
			{
				return null;
			}
		}
		
		//now to iterate through the tree if the first node isnt the one to remove
		//had to check if the first was the original node itself
		//this simply moves that
		//if the building we are looking for is older, and there are older buildings in tree
		if (this.data.yearOfConstruction > b.yearOfConstruction && this.older != null)
		{
			this.older = this.older.removeBuilding(b);
		}
		//if same and there are same branch
		if (this.data.yearOfConstruction == b.yearOfConstruction && this.same != null)
		{
			this.same = this.same.removeBuilding(b);
		}
		//if younger and there exists younger
		if (this.data.yearOfConstruction < b.yearOfConstruction && this.younger != null)
		{
			this.younger = this.younger.removeBuilding(b);
		}
		return this;
	}
	
	public int oldest(){
		Building checker = this;
		while (checker.older != null)
		{
			checker = checker.older;
		}
		if (checker.older == null)
		{
			return checker.data.yearOfConstruction;
		}
		return 0; 
	}
	
	public int highest(){
		
		int currentHighest = this.data.height;
		
		//check all 3 cases
		//include the highest within to recursively evaluate through branch
		if (this.same != null && this.same.highest() > currentHighest)
		{
			currentHighest = this.same.highest();
		}
		if (this.older != null && this.older.highest() > currentHighest)
		{
			currentHighest = this.older.highest();
		}
		if (this.younger != null &&  this.younger.highest() > currentHighest)
		{
			currentHighest = this.younger.highest();
		}
		return currentHighest; 
	}
	
	public OneBuilding highestFromYear (int year){
		//remember the root is the tallest of the respective year
		//we just need to find the first case of year being appropriate
		if (this.data.yearOfConstruction == year)
		{
			return this.data;
		}
		
		//there is no need for same because any same underneath would be lower
		
		//year is bigger than passed, and there exist youngers
		else if (this.data.yearOfConstruction < year && this.younger != null)
		{
			return this.younger.highestFromYear(year);
		}
		//year is smaller than passed, and there exists olders
		else if (this.data.yearOfConstruction > year && this.older != null)
		{
			return this.older.highestFromYear(year);
		}
		//couldnt find year
		else 
		{
			return null;
		}
	}
	
	public int numberFromYears (int yearMin, int yearMax){
		
		if(yearMin>yearMax)
		{
		return 0; 
		}
		int count=0;
		//count whenever we enter recursion enters fct and respesct years
		if (this.data.yearOfConstruction <= yearMax && this.data.yearOfConstruction >= yearMin)
		{
			count++;
		}
		//traverse all 3 branches within the years
		if (this.same != null)
		{
			count = count + this.same.numberFromYears(yearMin, yearMax);
		}
		if (this.older != null)
		{
			count = count + this.older.numberFromYears(yearMin, yearMax);
		}
		if (this.younger != null)
		{
			count = count + this.younger.numberFromYears(yearMin, yearMax);
		}
		
		return count;
	}
	
	public int[] costPlanning (int nbYears){
		int [] cost = new int[nbYears];
		for (int i = 0; i<nbYears; i++)
		{
			cost[i] = this.repairCostsIn (2018+i);
		}
			
		return cost;
	}
	public int repairCostsIn ( int year)
	{
		int repairCost = 0;
		//just like number of buildings
		//recursive ticks counter every time gets entered
		//create all 3 cases where it can be called recursively
		if (this.data.yearForRepair == year)
		{
			repairCost = repairCost + this.data.costForRepair;
		}
		//go down branches until find appropriate year
		//only then will above add to cost
		if (this.same != null)
		{
			repairCost = repairCost + this.same.repairCostsIn(year);
		}
		if (this.older != null)
		{
			repairCost = repairCost + this.older.repairCostsIn(year);
		}
		if (this.younger != null)
		{
			repairCost = repairCost + this.younger.repairCostsIn(year);
		}
		return repairCost;
	}
}
