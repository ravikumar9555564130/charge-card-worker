package org.camunda.bpm.getstarted.chargecard;

import java.util.logging.Logger;
import java.awt.Desktop;
import java.net.URI;

import org.camunda.bpm.client.ExternalTaskClient;

//use below URL in postmant with json data(resources/input_data.json) with post request .

//http://localhost:8080/engine-rest/process-definition/key/payment-retrieval/start

public class ChargeCardWorker {
	private final static Logger LOGGER = Logger.getLogger(ChargeCardWorker.class.getName());

	public static void main(String[] args) {
		ExternalTaskClient client = ExternalTaskClient.create().baseUrl("http://localhost:8080/engine-rest")
				.asyncResponseTimeout(10000) // long polling timeout
				.build();

		// subscribe to an external task topic as specified in the process
		client.subscribe("charge-card").lockDuration(1000) // the default lock duration is 20 seconds, but you can
															// override this
				.handler((externalTask, externalTaskService) -> {
					// Put your business logic here

					// Get a process variable
					String item = (String) externalTask.getVariable("item");
					Long amount = (Long) externalTask.getVariable("amount");
					String name = (String) externalTask.getVariable("name");
					String city = (String) externalTask.getVariable("city");

					LOGGER.info(
							"Charging credit card with an amount of '" + amount + "'� for the item '" + item + "'...");
					LOGGER.info(
							" name :: " + name + " city:: " + city + "'...");

					try {
						Desktop.getDesktop()
								.browse(new URI("https://docs.camunda.org/get-started/quick-start/complete"));
					} catch (Exception e) {
						e.printStackTrace();
					}

					// Complete the task
					externalTaskService.complete(externalTask);
				}).open();
	}
}