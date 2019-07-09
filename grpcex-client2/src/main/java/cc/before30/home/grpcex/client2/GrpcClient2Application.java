package cc.before30.home.grpcex.client2;

import cc.before30.home.grpc.proto.GreeterOuterClass;
import cc.before30.home.grpcex.client2.service.GrpcClient;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.sun.tools.javac.util.List;
import io.grpc.CallOptions;
import io.grpc.binarylog.v1.MessageOrBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * GrpcClient2Application
 *
 * @author before30
 * @since 2019-07-09
 */

@SpringBootApplication
public class GrpcClient2Application implements CommandLineRunner {

    @Autowired
    @Qualifier("grpcStreamSayHelloClient")
    GrpcClient grpcStreamSayHelloClient;

    @Autowired
    @Qualifier("grpcSayHelloClient")
    GrpcClient grpcSayHelloClient;

    @Autowired
    @Qualifier("grpcMultiSayHelloClient")
    GrpcClient grpcMultiSayHelloClient;

    public static void main(String[] args) {
        SpringApplication.run(GrpcClient2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        GreeterOuterClass.HelloRequest request = GreeterOuterClass.HelloRequest.newBuilder().setName("Joseph").build();

//        (MessageOrBuilder)request;
//
//        ImmutableList.builder().add((MessageOrBuilder)request);
//        ImmutableList<MessageOrBuilder> requests = new RegularImmutableList
//
//        grpcSayHelloClient.call(ImmutableList.of(request), null, CallOptions.DEFAULT)
    }
}
