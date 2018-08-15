package pingali.jeevan.alpakka.cassandra.first;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.alpakka.cassandra.CassandraBatchSettings;
import akka.stream.alpakka.cassandra.javadsl.CassandraFlow;
import akka.stream.alpakka.cassandra.javadsl.CassandraSource;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.datastax.driver.core.*;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Insert1 {
    public static void main(String args[]) throws InterruptedException, ExecutionException, TimeoutException {
        final Session session =
                Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).build().connect();

        final ActorSystem system = ActorSystem.create();
        final Materializer materializer = ActorMaterializer.create(system);


        final PreparedStatement preparedStatement =
                session.prepare("insert into cycling.personal_info (id, name, dob) values (?, ?, ?)");

        BiFunction<PersonalInfo, PreparedStatement, BoundStatement> statementBinder =
                (personalInfo, statement) -> statement.bind(personalInfo.getId(), personalInfo.getName(), personalInfo.getDob());

        PersonalInfo personalInfo = new PersonalInfo(2, "Jeevan", "18-08-1999");

        Random random = new Random(999999);

        Source<PersonalInfo, NotUsed> source =
                Source.from(
                        IntStream.range(1, 10000)
                                .boxed()
                                .map(i -> new PersonalInfo(random.nextInt(), "name" + random.nextInt(), "18-02-1999"))
                                .collect(Collectors.toList()));

        final Flow<PersonalInfo, PersonalInfo, NotUsed> flow =
                CassandraFlow.createWithPassThrough(2, preparedStatement, statementBinder, session);

        CompletionStage<List<PersonalInfo>> listCompletionStage = source.via(flow).runWith(Sink.seq(), materializer);

        Set<Integer> ids = listCompletionStage.toCompletableFuture().get(3, TimeUnit.SECONDS)
                .stream().map(PersonalInfo::getId).collect(Collectors.toSet());

        for (Integer id :
                ids) {
            System.out.println("Inserted ID: " + id);
        }

        Thread.sleep(10000l);

        system.terminate();

        System.exit(0);

    }
}
