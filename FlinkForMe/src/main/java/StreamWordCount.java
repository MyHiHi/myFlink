import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class StreamWordCount {
  public static void main(String[] args) throws Exception {
    ParameterTool arg = ParameterTool.fromArgs(args);
    String host = "";
    int port = 0;
    try {
      host = arg.get("host");
      port = arg.getInt("port");
    } catch (Exception e) {
      host = "127.0.0.1";
      port = 2003;
    }
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStream<String> text = env.socketTextStream(host, port);
    DataStream<Tuple2<String, Integer>> dataStream = text
        .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
          private static final long serialVersionUID = 1L;

          public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
            String[] tokens = s.toLowerCase().split("\\W+");
            for (String token : tokens) {
              if (token.length() > 0) {
                collector.collect(new Tuple2<String, Integer>(token, 1));
              }
            }
          }
        }).keyBy(0).sum(1);

    dataStream.print();
    // execute program
    env.execute("Java WordCount from SocketTextStream Example");

  }
}