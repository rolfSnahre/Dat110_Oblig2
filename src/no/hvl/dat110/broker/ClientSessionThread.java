package no.hvl.dat110.broker;

import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.messages.*;

public class ClientSessionThread extends Stopable{
	
	

	ClientSession client;
	
	Dispatcher dispatcher;
	

	public ClientSessionThread(String name, ClientSession client, Dispatcher dispatcher) {
		super(name);
		this.client = client;
		this.dispatcher = dispatcher;
	}


	@Override
	public void doProcess() {
		
		Message msg = null;
		if (client.hasData()) {
			msg = client.receive();
		}

		if (msg != null) {
			dispatcher.dispatch(client, msg);
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
}
