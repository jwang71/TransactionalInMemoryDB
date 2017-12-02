
package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import inmemorydb.InMemoryDB;

class InMemoryDBTest {
	InMemoryDB tc = new InMemoryDB();
	
	@Test
	public void test() {
//		tc.set("a", 10);
//		int ia = tc.get("a");
//		assertEquals(ia, 10);
//		tc.delete("a");
//		Integer ia2 = tc.get("a");
//		assertEquals(ia2, null);
		
		tc.begin();
		tc.set("a", 10);
		int ia3 = tc.get("a");
		assertEquals(ia3, 10);
		tc.begin();
		tc.set("a", 20);
		int kar = tc.get("a");
		assertEquals(kar, 20);
		tc.rollback();
		
		int ia4 = tc.get("a");
		assertEquals(ia4, 20);
		
		tc.rollback();
		int ia5 = tc.get("a");
		assertEquals(ia5, 10);
		
//		
//		tc.begin();
//		tc.set("a", 30);
//		tc.begin();
//		tc.set("a", 40);
//		tc.commit();
//		int ia = tc.get("a");
//		assertEquals(ia, 40);
		
		
		tc.set("a", 50);
		
		tc.begin();
		int ia8 = tc.get("a");
		assertEquals(ia8, 50);
		tc.set("a", 60);
		tc.begin();
		tc.delete("a");
		Integer ia6 = tc.get("a");
		assertEquals(ia6, null);
		
//		tc.rollback();
//		int ia7 = tc.get("a");
//		assertEquals(ia7, 60);
	}	
}


