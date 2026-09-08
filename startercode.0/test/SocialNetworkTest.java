import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SocialNetworkTest {

	SocialNetwork sn;
	Account me;
	Account her;

	@Before
	public void setUp() throws Exception {
		sn = new SocialNetwork();
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test 
	public void joinReturnsAccountWithGivenUserName() {
		me = sn.join("Hakan");
		assertEquals("Hakan", me.getUserName());
	}
	
	@Test 
	public void listMembersAfterOneJoinContainsThatMember() {
		sn.join("Hakan");
		Collection<String> members = sn.listMembers();
		assertEquals(1, members.size());
		assertTrue(members.contains("Hakan"));
	}
	
	@Test 
	public void listMembersAfterTwoJoinsContainsBothMembers() {
		joinHakanAndCecile();
		Collection<String> members = sn.listMembers();
		assertEquals(2, members.size());
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}
	
	@Test 
	public void sendFriendshipToAddsRequesterToIncomingRequests() {
		joinHakanAndCecile();
		sn.sendFriendshipTo("Cecile", me);
		assertTrue(her.getIncomingRequests().contains("Hakan"));
	}
	
	@Test 
	public void acceptFriendshipFromAddsRequesterToAcceptersFriends() {
		joinHakanAndCecile();
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(her.hasFriend("Hakan"));
	}

	@Test 
	public void acceptFriendshipFromAddsAccepterToRequestersFriends() {
		joinHakanAndCecile();
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
	}

	@Test
	public void emptyNetworkHasNoMembers() {
		assertEquals(0, sn.listMembers().size());
	}

	@Test
	public void joinWithExistingUserNameReturnsNull() {
		sn.join("Hakan");
		assertNull(sn.join("Hakan"));
	}

	@Test
	public void joinWithExistingUserNameLeavesMemberCountUnchanged() {
		sn.join("Hakan");
		sn.join("Hakan");
		assertEquals(1, sn.listMembers().size());
	}

	@Test
	public void joinWithNullUserNameReturnsNull() {
		assertNull(sn.join(null));
	}

	@Test
	public void joinWithEmptyUserNameReturnsNull() {
		assertNull(sn.join(""));
	}

	@Test
	public void sendFriendshipToUnknownUserDoesNotAddIncomingRequest() {
		me = sn.join("Hakan");
		sn.sendFriendshipTo("Ghost", me);
		assertEquals(0, me.getIncomingRequests().size());
	}

	@Test
	public void sendFriendshipToSelfDoesNotAddIncomingRequest() {
		me = sn.join("Hakan");
		sn.sendFriendshipTo("Hakan", me);
		assertEquals(0, me.getIncomingRequests().size());
	}

	@Test
	public void acceptFriendshipFromUnknownUserDoesNotAddFriends() {
		me = sn.join("Hakan");
		sn.acceptFriendshipFrom("Ghost", me);
		assertEquals(0, me.getFriends().size());
	}

	@Test
	public void acceptFriendshipFromWithoutPendingRequestDoesNotAddFriends() {
		joinHakanAndCecile();
		sn.acceptFriendshipFrom("Hakan", her);
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));
	}

	private void joinHakanAndCecile() {
		me = sn.join("Hakan");
		her = sn.join("Cecile");
	}

}
