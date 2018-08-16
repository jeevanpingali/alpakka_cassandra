# Alpakka Cassandra

This is a sample program that uses Cassandra Alpakka libraries to read data (CassandraSource) and write data (CassandraFlow) using the documentation at: https://developer.lightbend.com/docs/alpakka/current/cassandra.html

While implementing Insert1, to insert data into my local Cassandra, I've run into some performance issues. I'm not yet sure if those issues are becasue of the hardware limitations, incorrect Cassandra (DSE version) or the program/Akka configuration settings.
