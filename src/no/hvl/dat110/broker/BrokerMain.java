package no.hvl.dat110.broker;

public class BrokerMain {

	public static void main(String[] args) {
		Runnable broker = () -> BrokerServer.main(null);
		broker.run();

	}

}
