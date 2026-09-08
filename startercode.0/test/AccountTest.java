import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class AccountTest {
	
	Account me, her, another;
	
	@Before
	public void setUp() throws Exception {
		me = new Account("Hakan");
		her = new Account("Serra");
		another = new Account("Cecile");
	}

	@Test
	public void requestFriendshipAddsRequesterToIncomingRequests() {
		me.requestFriendship(her);
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void newAccountHasNoIncomingRequests() {
		assertEquals(0, me.getIncomingRequests().size());
	}
	
	@Test
	public void twoRequestersAppearInIncomingRequests() {
		me.requestFriendship(her);
		me.requestFriendship(another);
		assertEquals(2, me.getIncomingRequests().size());
		assertTrue(me.getIncomingRequests().contains(another.getUserName()));
		assertTrue(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void duplicateRequestKeepsIncomingSizeAtOne() {
		me.requestFriendship(her);
		me.requestFriendship(her);
		assertEquals(1, me.getIncomingRequests().size());
	}
	
	@Test
	public void acceptingRequestRemovesItFromIncomingRequests() {
		me.requestFriendship(her);
		her.friendshipAccepted(me);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
	}
	
	@Test
	public void acceptedRequestMakesBothAccountsFriends() {
		becomeFriends(her, me);
		assertAreFriends(me, her);
	}

	@Test
	public void accountCanHaveMultipleFriends() {
		becomeFriends(her, me);
		becomeFriends(another, me);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(me.hasFriend(another.getUserName()));
	}
	
	@Test
	public void requestingExistingFriendAddsNoIncomingRequest() {
		becomeFriends(her, me);
		me.requestFriendship(her);
		assertFalse(me.getIncomingRequests().contains(her.getUserName()));
	}

	@Test
	public void newAccountHasNoFriends() {
		assertEquals(0, me.getFriends().size());
	}

	@Test
	public void selfRequestDoesNotAddIncomingRequest() {
		me.requestFriendship(me);
		assertFalse(me.getIncomingRequests().contains(me.getUserName()));
	}

	@Test
	public void acceptWithoutPendingRequestDoesNotAddFriends() {
		her.friendshipAccepted(me);
		assertFalse(me.hasFriend(her.getUserName()));
		assertFalse(her.hasFriend(me.getUserName()));
	}

	@Test
	public void acceptClearsReverseIncomingRequest() {
		me.requestFriendship(her);
		her.requestFriendship(me);
		her.friendshipAccepted(me);
		assertFalse(her.getIncomingRequests().contains(me.getUserName()));
	}

	@Test
	public void newAccountHasNoOutgoingRequests() {
		assertEquals(0, me.getOutgoingRequests().size());
	}

	@Test
	public void requestFriendshipAddsReceiverToSendersOutgoingRequests() {
		me.requestFriendship(her);
		assertTrue(her.getOutgoingRequests().contains(me.getUserName()));
	}

	@Test
	public void acceptedRequestRemovesReceiverFromOutgoingRequests() {
		me.requestFriendship(her);
		her.friendshipAccepted(me);
		assertFalse(her.getOutgoingRequests().contains(me.getUserName()));
	}

	@Test
	public void selfRequestDoesNotAddOutgoingRequest() {
		me.requestFriendship(me);
		assertFalse(me.getOutgoingRequests().contains(me.getUserName()));
	}

	@Test
	public void requestingExistingFriendAddsNoOutgoingRequest() {
		becomeFriends(her, me);
		me.requestFriendship(her);
		assertFalse(her.getOutgoingRequests().contains(me.getUserName()));
	}

	private void becomeFriends(Account requester, Account receiver) {
		receiver.requestFriendship(requester);
		requester.friendshipAccepted(receiver);
	}

	private void assertAreFriends(Account one, Account other) {
		assertTrue(one.hasFriend(other.getUserName()));
		assertTrue(other.hasFriend(one.getUserName()));
	}

}
