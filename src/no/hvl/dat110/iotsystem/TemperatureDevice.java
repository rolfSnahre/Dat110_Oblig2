package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;

public class TemperatureDevice {
	
	private static final int COUNT = 10;
	
	public static void main(String[] args) {
		
		TemperatureSensor sn = new TemperatureSensor();
		
		// TODO - start
		
		String user = "tempSensor";
		String server = Common.BROKERHOST;
		int port = Common.BROKERPORT;
		
		Client client = new Client(user, server, port);
		if(!client.connect()) {
			return;
		}
		
		String topic = Common.TEMPTOPIC;
		
		for(int n = 0; n < COUNT; n++) {
			client.publish(topic, String.valueOf(sn.read())); 
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		client.disconnect();
		
		// TODO - end
		
		System.out.println("Temperature device stopping ... ");
		
		
	}
}
