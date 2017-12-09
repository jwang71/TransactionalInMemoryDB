
package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import inmemorydb.InMemoryDB;

class InMemoryDBTest {
	InMemoryDB tc = new InMemoryDB();
	
	@Test
	public void test() {
		tc.set("a", 10);
		int ia = tc.get("a");
		assertEquals(ia, 10);
		System.out.println(tc.count(10));
		
		tc.delete("a");
		Integer ia2 = tc.get("a");
		assertEquals(ia2, null);
		System.out.println(tc.count(10));
	
		tc.begin();
		tc.set("a", 20);
		int ia3 = tc.get("a");
		assertEquals(ia3, 20);
		System.out.println(tc.count(20));
		System.out.println(tc.count(10));

		tc.begin();
		tc.set("a", 30);
		int kar = tc.get("a");
		assertEquals(kar, 30);
		System.out.println(tc.count(30));

		tc.rollback();
		
		int ia4 = tc.get("a");
		assertEquals(ia4, 20);
		System.out.println(tc.count(20));
	
		tc.rollback();
		Integer ia5 = tc.get("a");
		assertEquals(ia5, null);
		System.out.println(tc.count(20));
		
		tc.begin();
		tc.set("a", 30);
		tc.begin();
		tc.set("a", 40);
		tc.commit();
		int ia6 = tc.get("a");
		assertEquals(ia6, 40);
		System.out.println(tc.count(40));
		System.out.println(tc.count(30));
		tc.rollback();
		int ia7 = tc.get("a");
		assertEquals(ia7, 40);
		System.out.println(tc.count(40));

		tc.set("a", 50);
		tc.set("b", 60);
		tc.set("c", 60);
		tc.set("d", 50);
		tc.begin();
		int ia8 = tc.get("a");
		assertEquals(ia8, 50);
		tc.set("a", 60);
		tc.begin();
		tc.set("a", 40);
 		System.out.println(tc.count(50));
		System.out.println(tc.count(60));
		System.out.println(tc.count(40));

		tc.begin();
		tc.set("b", 1);
		tc.delete("a");
     	System.out.println(tc.count(60));
	
		System.out.println(tc.checkTransaction());		
//
//		Integer ia9 = tc.get("a");
//		assertEquals(ia9, null);
//		int ib = tc.get("b");
//		assertEquals(ib, 1);
//		tc.rollback();
//	//	System.out.println(tc.get("a"));
//		int ia10 = tc.get("a");
//		assertEquals(ia10, 60);
	}	
}


