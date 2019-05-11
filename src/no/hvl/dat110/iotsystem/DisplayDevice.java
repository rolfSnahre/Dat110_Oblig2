package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messages.PublishMsg;

public class DisplayDevice {
	
	private static final int COUNT = 10;
		
	public static void main (String[] args) {
		
		System.out.println("Display starting ...");
		
		// TODO - START
		
		String user = "DisplayDevice";
		String server = Common.BROKERHOST;
		int port = Common.BROKERPORT;
		
		Client client = new Client(user, server, port);
		if(!client.connect()) {
			return;
		}

		String topic = Common.TEMPTOPIC;
		
		client.createTopic(topic);
		client.subscribe(topic);
		
		for(int n = 0; n < COUNT; n++) {
			System.out.println("Display message: " + client.receive());
		}
		
		client.disconnect();
		
		// TODO - END
		
		System.out.println("Display stopping ... ");
		
		
	}
}
