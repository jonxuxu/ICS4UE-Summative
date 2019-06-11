import java.util.ArrayList;

public class RegionLayer {
	public ArrayList<Region> regions;
	
	public RegionLayer() {
		this.regions = new ArrayList<Region>(0);
	}
	
	public ArrayList<Region> getRawData() {
		return this.regions;
	}
	
	public String checkCoordinate(int x, int y) {
		ArrayList<Region> valid = new ArrayList<Region>(0);
		for (int idx = 0; idx < regions.size(); idx++) {
			if (regions.get(idx).contains(x,y)) {
				valid.add(regions.get(idx));
			}
		}
		
		if (valid.size() == 0) {
			return "none";
		} else if (valid.size() == 1) {
			return valid.get(0).regionType;
		} else {			
			int highestPriorityIdx = 0;
			
			for (int idx = 1; idx < valid.size(); idx++) {
				if (valid.get(idx).regionPriority > valid.get(highestPriorityIdx).regionPriority) {
					highestPriorityIdx = idx;
				}
			}
			
			return valid.get(highestPriorityIdx).regionType;
		}
		
	}			
}
