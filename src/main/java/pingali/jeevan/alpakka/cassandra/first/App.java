package pingali.jeevan.alpakka.cassandra.first;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.alpakka.cassandra.javadsl.CassandraSource;
import akka.stream.javadsl.Sink;
import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.OperationTimedOutException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class App {
    public static void main(String args[]) throws InterruptedException, ExecutionException, TimeoutException {
        final Session session =
                Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).build().connect();

        final ActorSystem system = ActorSystem.create();
        final Materializer materializer = ActorMaterializer.create(system);

        final Statement stmt =
                new SimpleStatement("SELECT * FROM cycling.personal_info").setFetchSize(10000);

        final CompletionStage<List<Row>> rows =
                CassandraSource.create(stmt, session).runWith(Sink.seq(), materializer);

        Set<Integer> ids = rows.toCompletableFuture()
                .get(3, TimeUnit.SECONDS)
                .stream()
                .map(r -> r.getInt("id"))
                .collect(Collectors.toSet());

        for (Integer id: ids) {
            System.out.println("ID: " + id);
        }

        Thread.sleep(5000l);

        system.terminate();

        System.exit(0);

    }
}
