
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class WordCount {
  public static void main(String[] args) throws Exception {
    ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

    DataSource<String> text = env.readTextFile("D:\\flink\\FlinkForMe\\src\\main\\java\\file.txt");

    AggregateOperator<Tuple2<String, Integer>> dataStream = text
        .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
          private static final long serialVersionUID = 1L;

          public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] tokens = value.toLowerCase().split(" ");
            for (String token : tokens) {
              if (token.length() > 0) {
                out.collect(new Tuple2<String, Integer>(token, 1));
              }
            }

          }

        }).groupBy(0).sum(1);
    dataStream.print();

  }
}
