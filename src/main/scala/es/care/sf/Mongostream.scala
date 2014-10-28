package es.care.sf

import reactivemongo.core.nodeset.Authenticate
import reactivemongo.bson.BSONDocument
import scala.concurrent.Future
import play.api.libs.iteratee.Iteratee
import reactivemongo.bson.BSONString
import java.util.Calendar
import reactivemongo.bson.BSONTimestamp
import reactivemongo.bson.BSONTimestamp
import reactivemongo.api.QueryOpts

object Mongostream  {
  
    val host = "fintonicdev1.cloudapp.net"
     
    // credentials  
    val auth_dbName = "admin"
    val userName = "monstream"
    val password = "monstream"  
      
    //use db and collection  
    val effective_dbName = "local"
    val collectionName = "oplog.rs"
  
 
  def connect() = {
    import reactivemongo.api._
    import scala.concurrent.ExecutionContext.Implicits.global

  
      
    val driver = new MongoDriver

    val credentials = List(Authenticate(auth_dbName, userName, password))
    val connection = driver.connection(List(host), authentications = credentials)

   

    // Gets a reference to the database 
    connection(effective_dbName)
  }
  
  def listDocs() = {
    
    import scala.concurrent.ExecutionContext.Implicits.global
    
    val db = connect()
    val collection = db(collectionName)
    
    // Select only the documents which field 'firstName' equals 'Jack'
    //val query = BSONDocument("bankId" -> "0182")
    // select only the fields 'lastName' and '_id'
    //val filter = BSONDocument(     
    //  "_id" -> -1)

    val pivot_ts =  null
    
    val collection_ns = "business.transaction"
    
    //1412950139888
    val query =  BSONDocument(
    				"ns" -> collection_ns
    				//"ts" -> BSONDocument("$gte" -> pivot_ts)	
    			)
    			
    val filter = BSONDocument()
    
    /* Let's run this query then enumerate the response and print a readable
     * representation of each document in the response */
    collection.
      find(query, filter).
      options(QueryOpts().tailable.awaitData).
      cursor[BSONDocument].
      enumerate().apply(Iteratee.foreach { doc =>
        println("found document: " + BSONDocument.pretty(doc))
        KafkaProducer.producer.send(collectionName, BSONDocument.pretty(doc))
      })
      
    println("After first query")   

    /*  // Or, the same with getting a list
    val futureList: Future[List[BSONDocument]] =
      collection.
        find(query, filter).
        cursor[BSONDocument].
        collect[List]()

    futureList.map { list =>
      list.foreach { doc =>
        println("found document: " + BSONDocument.pretty(doc))
      }
    }*/
    
   
    
  }
  
   def main(args: Array[String]): Unit = {
     
      println("Hello, mongoStream")  
    
      listDocs() 
  
     
   }   
  
  
}
