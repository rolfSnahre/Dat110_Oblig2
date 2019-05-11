package no.hvl.dat110.broker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import no.hvl.dat110.common.Logger;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messagetransport.Connection;

public class Storage {

	protected ConcurrentHashMap<String, Set<String>> subscriptions;
	protected ConcurrentHashMap<String, ClientSession> clients;
	protected ConcurrentHashMap<String, Set<Message>> messageBuff;
	protected ConcurrentHashMap<String, ClientSessionThread> threads;

	public Storage() {
		subscriptions = new ConcurrentHashMap<String, Set<String>>();
		clients = new ConcurrentHashMap<String, ClientSession>();
		messageBuff = new ConcurrentHashMap<String, Set<Message>>();
		threads = new ConcurrentHashMap<String, ClientSessionThread>();
	}

	public Collection<ClientSession> getSessions() {
		return clients.values();
	}

	public Set<String> getTopics() {

		return subscriptions.keySet();

	}

	public ClientSession getSession(String user) {

		ClientSession session = clients.get(user);

		return session;
	}

	public Set<String> getSubscribers(String topic) {

		return (subscriptions.get(topic));
	}

	public void addClientSession(String user, Connection connection) {

		// TODO: add corresponding client session to the storage
		clients.put(user, new ClientSession(user, connection));
		return;
	}

	public void removeClientSession(String user) {

		// TODO: remove client session for user from the storage
		clients.remove(user);
		return;
	}

	public void createTopic(String topic) {

		// TODO: create topic in the storage
		subscriptions.put(topic, new HashSet<String>());
		return;
	}

	public void deleteTopic(String topic) {

		// TODO: delete topic from the storage
		subscriptions.remove(topic);
		return;		
	}

	public void addSubscriber(String user, String topic) {

		// TODO: add the user as subscriber to the topic
		subscriptions.get(topic).add(user);
		return;
	}

	public void removeSubscriber(String user, String topic) {

		// TODO: remove the user as subscriber to the topic
		subscriptions.get(topic).remove(user);
		return;
	}
	
	public void addBuffer(String user) {
		if(messageBuff.containsKey(user)) {
			messageBuff.put(user, new HashSet<Message>());
		}
	}
	
	public void bufferMessage(String user, Message msg) {
		messageBuff.get(user).add(msg);
	}
	
	public Set<Message> getBufferedMessages (String user){
		if(messageBuff.containsKey(user)) {
			return messageBuff.get(user);
		}else {
			return new HashSet<Message>();
		}
	}
	
	public void addThread(String user, ClientSessionThread cliantThread) {
		threads.put(user, cliantThread);
	}
	
	public ClientSessionThread getThread(String user) {
		return threads.get(user);
	}
}
