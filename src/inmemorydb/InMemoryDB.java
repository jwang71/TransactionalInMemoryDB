package inmemorydb;
import java.util.*;

public class InMemoryDB {
	private Map<String, Integer> map = new HashMap<>();
	private Map<Integer, Set<String>> countMap = new HashMap<>();

	private Map<Integer, Set<String>> tempCountMap = new HashMap<>();
	private Stack<Map<String, Integer>> stack = new Stack<>();
	
	public void set(String key, Integer value) {
		if (checkTransaction()) {
			Map<String, Integer> curTran = stack.pop();
			if (curTran.containsKey(key)) {
				Set<String> set = tempCountMap.get(curTran.get(key));
				set.remove(key);
				tempCountMap.put(curTran.get(key), set);				
			}
			curTran.put(key, value);
			stack.push(curTran);
			
			if (tempCountMap.containsKey(value)) {
				Set<String> temp = tempCountMap.get(value);
				temp.add(key);
				tempCountMap.put(value, temp);
			} else {
				Set<String> temp = new HashSet<String>();
				temp.add(key);
				tempCountMap.put(value, temp);
			}
		} else {
			if (map.containsKey(key)) {
				Set<String> set = countMap.get(map.get(key));
				set.remove(key);
				countMap.put(map.get(key), set);				
			}
			
			map.put(key, value);
			
			if (countMap.containsKey(value)) {
				Set<String> temp = countMap.get(value);
				temp.add(key);
				countMap.put(value, temp);
			} else {
				Set<String> temp = new HashSet<String>();
				temp.add(key);
				countMap.put(value, temp);
			}
		}
	}
	
	public Integer get(String key) {
		if (checkTransaction()) {
			Map<String, Integer> curTran = stack.peek();
			for (int i = stack.size() - 1; i >= 0; i--) {
				if (stack.get(i).containsKey(key)) {
					return stack.get(i).get(key);
				}
			}
			if (map.containsKey(key)) return map.get(key);
			return null;		
		} else {
			return map.containsKey(key) ? map.get(key) : null;			
		}
	}
	
	public void delete(String key) {
		if (checkTransaction()) {
			Map<String, Integer> curTran = stack.peek();
//			if (curTran.containsKey(key)) {
//				curTran.remove(key);
//			}
			curTran.put("a", null);
		} else {
			Set<String> temp = countMap.get(map.get(key));
			temp.remove(key);
			countMap.put(map.get(key), temp);
			map.remove(key);		
		}
	}
	
	public int count(Integer value) {
		int number = 0;
		if (checkTransaction()) {
			//TODO			
			if (!countMap.containsKey(value) && !tempCountMap.containsKey(value)) {
				return 0;
			}
			if (countMap.containsKey(value) && tempCountMap.containsKey(value)) {
				int common = 0;
				Set<String> set = countMap.get(value);
				Set<String> tempset = tempCountMap.get(value);
				for (String s : set) {
					if (tempset.contains(s)) {
						common++;
					}
				}
				number = set.size() + tempset.size() - common;				
			} else if (countMap.containsKey(value)){
				number = countMap.get(value).size();
			} else {
				number = tempCountMap.get(value).size();
			}
		} else {
			//TODO
			number = countMap.containsKey(value) ? countMap.get(value).size() : 0;
		}
		return number;
	}
	
	public void begin() {
		Map<String, Integer> tempMap = new HashMap<String, Integer>();
		stack.push(tempMap);
	}
	
	public void rollback() {
		if (!isCommitted()) {
			stack.pop();			
		}
	}
	
	public void commit() {
		Stack<Map<String, Integer>> reverseStack = new Stack<>();
		Set<String> visited = new HashSet<String>();
		
		while (!stack.isEmpty()) {
			Map<String, Integer> curTran = stack.pop();
			//Update countMap
			for (Map.Entry<String, Integer> entry : curTran.entrySet()) {
				if (!visited.contains(entry.getKey())) {
					if (countMap.containsKey(entry.getValue())) {
						Set<String> set = countMap.get(entry.getValue());
						set.add(entry.getKey());
						countMap.put(entry.getValue(), set);
					} else {
						Set<String> set = new HashSet<>();
						set.add(entry.getKey());
						countMap.put(entry.getValue(), set);			
					}
					visited.add(entry.getKey());
				}
			}
			reverseStack.add(curTran);
		}
		
		while (!reverseStack.isEmpty()) {
			Map<String, Integer> curTran = reverseStack.pop();
			
			for (Map.Entry<String, Integer> entry : curTran.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}
		}		
	}
	
	public boolean checkTransaction() {
//		if (!stack.isEmpty()) {
//			for (Map<String,Integer> m : stack) {
//				if (m.size() != 0) {
//					return true;
//				}	
//			}			
//		}
		return stack.isEmpty() ? false : true;
	}
	
	private boolean isCommitted() {
		return stack.isEmpty() ? true : false;
	}
}