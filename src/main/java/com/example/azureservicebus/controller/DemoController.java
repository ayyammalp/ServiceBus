package com.example.azureservicebus.controller;

import org.apache.qpid.jms.message.JmsBytesMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.Message;

@RestController
public class DemoController {

	private static final String QUEUE_NAME = "stdqueue";

	final static Logger logger = LoggerFactory.getLogger(DemoController.class);

	private static String connString = "HostName=WiliotSampleretailiothub.azure-devices.net;DeviceId=WiliotretailDevice;SharedAccessKey=d6ReQ94UVYRrQAQCf6uZbZ8TTrgHHqyZu4ySUBzx9OQ=";

	private static IotHubClientProtocol protocol = IotHubClientProtocol.HTTPS;// or HTTPS

	@JmsListener(destination = QUEUE_NAME, containerFactory = "jmsListenerContainerFactory")
	public void receiveMessage(JmsBytesMessage message) {

		int length;
		try {

			length = new Long(message.getBodyLength()).intValue();
			byte[] b = new byte[length];
			message.readBytes(b, length);
			String text = new String(b, "UTF-8");
			logger.info("Received message: {}", text);

			DeviceClient client = new DeviceClient(connString, protocol);
			
			client.open(true);
			Message msg = new Message(text);

			System.out.println(text);

			client.sendEventAsync(msg, null, null);

		} catch (Exception e) {
			// TODO Auto-generated catch block

			// serviceClient.close();
			e.printStackTrace();

		}

	}

	
	// logger.info("Received message: {}", respberrymsg.toString());

	@RequestMapping("/")
	public String index() {
		return "Greetings from Azure Spring Cloud!";
	}
}
