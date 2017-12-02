package inmemorydb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class InMemoryDB {
	private Map<String, Integer> map = new HashMap<>();
	private Map<Integer, Integer> countMap = new HashMap<>();
	private Map<String, Integer> tempMap;
	private Map<Integer, Integer> tempCountMap = new HashMap<>();
	private Stack<Map<String, Integer>> stack = new Stack<>();
	
	public void set(String key, Integer value) {
		if (checkTransaction()) {
			Map<String, Integer> curTran = stack.peek();
			curTran.put(key, value);
			if (tempCountMap.containsKey(key)) {
				tempCountMap.put(value, tempCountMap.get(key) + 1);
			} else {
				tempCountMap.put(value, 1);
			}
		} else {
			map.put(key, value);
			if (countMap.containsKey(value)) {
				countMap.put(value, countMap.get(value) + 1);
			} else {
				countMap.put(value, 1);
			}
		}
	}
	
	public Integer get(String key) {
		if (checkTransaction()) {
			Map<String, Integer> curTran = stack.peek();
			return curTran.containsKey(key) ? curTran.get(key) : null;
		} else {
			return map.containsKey(key) ? map.get(key) : null;			
		}
	}
	
	public void delete(String key) {
		if (checkTransaction()) {
			Map<String, Integer> curTran = stack.peek();
			if (curTran.containsKey(key)) {
				curTran.remove(key);
			}
		} else {
			countMap.put(map.get(key), countMap.get(map.get(key)) -1);
			map.remove(key);		
		}
	}
	
	public int count(Integer value) {
		int number = 0;
		if (checkTransaction()) {
			//TODO
			number = countMap.get(value) + tempCountMap.get(value);
		} else {
			//TODO
			//number = countMap.containsKey(value) ? countMap.get(value).size() : 0;
			number = countMap.containsKey(value) ? countMap.get(value) : 0;
		}
		return number;
	}
	
	public void begin() {
		tempMap = new HashMap();
		stack.push(tempMap);
	}
	
	public void rollback() {
		stack.pop();
		
//		for (Map.Entry<String, Integer> entry : tempMap.entrySet()) {
//			curTran.remove(entry.getKey());
//		}
//		for (Map.Entry<Integer, Integer> entry : tempCountMap.entrySet()) {
//			tempCountMap.remove(entry.getKey());
//		}	

	}
	
	public void commit() {
		Stack<Map<String, Integer>> reverseStack = new Stack<>();
		while (!stack.isEmpty()) {
			reverseStack.add(stack.pop());
		}
		while (!reverseStack.isEmpty()) {
			Map<String, Integer> curTran = reverseStack.pop();
			for (Map.Entry<String, Integer> entry : curTran.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private boolean checkTransaction() {
		for (Map<String,Integer> m : stack) {
			if (!m.isEmpty()) {
				return true;
			}	
		}
		return false;
	}
}