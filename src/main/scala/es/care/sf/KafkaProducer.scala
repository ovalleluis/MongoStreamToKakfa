package es.care.sf

import java.util.Properties

import kafka.producer._

object KafkaProducer  {
  
   val broker = "fintonicdev1.cloudapp.net:9092"
   val topic = "oplog"
  

	object producer {
     
     
  
		val props = new Properties();
        props.put("metadata.broker.list", broker);
        props.put("serializer.class", "kafka.serializer.StringEncoder");    
        props.put("request.required.acks", "1");
        
        val config = new ProducerConfig(props)
        
        							//type of partition key, type of the message
        val producer = new Producer[String, String](config)
        
        def send(partitionKey: String, msg: String) = {          
          producer.send(new KeyedMessage[String, String]( topic , partitionKey, msg))
        }
        
	}

}