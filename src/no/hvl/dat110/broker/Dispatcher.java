package no.hvl.dat110.broker;

import java.util.Set;

import com.sun.security.ntlm.Client;

import java.util.Collection;

import no.hvl.dat110.common.Logger;
import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.messages.*;
import no.hvl.dat110.messagetransport.Connection;

public class Dispatcher extends Stopable {

	private Storage storage;

	public Dispatcher(Storage storage) {
		super("Dispatcher");
		this.storage = storage;

	}

//	@Override
//	public void doProcess() {
//
//		Collection<ClientSession> clients = storage.getSessions();
//
//		Logger.lg(".");
//		for (ClientSession client : clients) {
//
//			Message msg = null;
//
//			if (client.hasData()) {
//				msg = client.receive();
//			}
//
//			if (msg != null) {
//				dispatch(client, msg);
//			}
//		}
//
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//	
	
	@Override
	public void doProcess() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void dispatch(ClientSession client, Message msg) {

		MessageType type = msg.getType();

		switch (type) {

		case DISCONNECT:
			onDisconnect((DisconnectMsg) msg);
			break;

		case CREATETOPIC:
			onCreateTopic((CreateTopicMsg) msg);
			break;

		case DELETETOPIC:
			onDeleteTopic((DeleteTopicMsg) msg);
			break;

		case SUBSCRIBE:
			onSubscribe((SubscribeMsg) msg);
			break;

		case UNSUBSCRIBE:
			onUnsubscribe((UnsubscribeMsg) msg);
			break;

		case PUBLISH:
			onPublish((PublishMsg) msg);
			break;

		default:
			Logger.log("broker dispatch - unhandled message type");
			break;

		}
	}

	// called from Broker after having established the underlying connection
	public void onConnect(ConnectMsg msg, Connection connection) {

		String user = msg.getUser();
		
		Logger.log("onConnect:" + msg.toString());

		storage.addClientSession(user, connection);
		
		ClientSession clientSession = storage.getSession(user);
		
		Set<Message> msgs = storage.messageBuff.get(user);
		
		if(msgs != null) {
			for(Message message : msgs) {
				clientSession.send(message);
			}
			
		}
		
		ClientSessionThread cliantThread = new ClientSessionThread(user, clientSession, this);
		storage.addThread(user, cliantThread);
		
		cliantThread.start();
		try {
			cliantThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

	// called by dispatch upon receiving a disconnect message 
	public void onDisconnect(DisconnectMsg msg) {

		String user = msg.getUser();

		Logger.log("onDisconnect:" + msg.toString());

		storage.removeClientSession(user);

		storage.addBuffer(user);
		
		storage.getThread(user).doStop();
		
		
	}

	public void onCreateTopic(CreateTopicMsg msg) {

		Logger.log("onCreateTopic:" + msg.toString());

		// TODO: create the topic in the broker storage 
		storage.createTopic(msg.getTopic());
	}

	public void onDeleteTopic(DeleteTopicMsg msg) {

		Logger.log("onDeleteTopic:" + msg.toString());

		// TODO: delete the topic from the broker storage
		storage.deleteTopic(msg.getTopic());
	}

	public void onSubscribe(SubscribeMsg msg) {

		Logger.log("onSubscribe:" + msg.toString());

		// TODO: subscribe user to the topic
		storage.addSubscriber(msg.getUser(), msg.getTopic());

		
	}

	public void onUnsubscribe(UnsubscribeMsg msg) {

		Logger.log("onUnsubscribe:" + msg.toString());

		// TODO: unsubscribe user to the topic
		storage.removeSubscriber(msg.getUser(), msg.getTopic());
		
	}

	public void onPublish(PublishMsg msg) {

		Logger.log("onPublish:" + msg.toString());

		// TODO: publish the message to clients subscribed to the topic
		
		Set<String> subscribers = storage.getSubscribers(msg.getTopic());
		
		for(String user : subscribers) {
			ClientSession cs = storage.getSession(user);
			if(cs == null) {
				storage.bufferMessage(user, msg);
			}else {
				cs.send(msg);
			}
		}
		
	}

	
}
