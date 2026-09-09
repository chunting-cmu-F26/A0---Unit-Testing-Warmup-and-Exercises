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
	public void sendFriendshipToAddsTargetToOutgoingRequests() {
		joinHakanAndCecile();
		sn.sendFriendshipTo("Cecile", me);
		assertTrue(me.getOutgoingRequests().contains("Cecile"));
	}

	@Test
	public void acceptFriendshipFromRemovesTargetFromOutgoingRequests() {
		joinHakanAndCecile();
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertFalse(me.getOutgoingRequests().contains("Cecile"));
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

	@Test
	public void acceptAllFriendshipsToMakesAllRequestersFriends() {
		Account another = joinHakanCecileAndSerra();
		sn.sendFriendshipTo("Hakan", her);
		sn.sendFriendshipTo("Hakan", another);
		sn.acceptAllFriendshipsTo(me);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(me.hasFriend("Serra"));
	}

	@Test
	public void acceptAllFriendshipsToClearsIncomingRequests() {
		Account another = joinHakanCecileAndSerra();
		sn.sendFriendshipTo("Hakan", her);
		sn.sendFriendshipTo("Hakan", another);
		sn.acceptAllFriendshipsTo(me);
		assertEquals(0, me.getIncomingRequests().size());
	}

	@Test
	public void acceptAllFriendshipsToRemovesMeFromRequestersOutgoingRequests() {
		joinHakanAndCecile();
		sn.sendFriendshipTo("Hakan", her);
		sn.acceptAllFriendshipsTo(me);
		assertFalse(her.getOutgoingRequests().contains("Hakan"));
	}

	@Test
	public void acceptAllFriendshipsToWithNoPendingRequestsLeavesFriendsUnchanged() {
		joinHakanAndCecile();
		sn.acceptAllFriendshipsTo(me);
		assertEquals(0, me.getFriends().size());
	}

	@Test
	public void rejectFriendshipFromRemovesTargetFromOutgoingRequests() {
		joinHakanAndCecile();
		sn.sendFriendshipTo("Cecile", me);
		sn.rejectFriendshipFrom("Hakan", her);
		assertFalse(me.getOutgoingRequests().contains("Cecile"));
	}

	private void joinHakanAndCecile() {
		me = sn.join("Hakan");
		her = sn.join("Cecile");
	}

	private Account joinHakanCecileAndSerra() {
		joinHakanAndCecile();
		return sn.join("Serra");
	}

}
