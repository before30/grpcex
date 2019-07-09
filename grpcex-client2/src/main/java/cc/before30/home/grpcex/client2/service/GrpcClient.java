package cc.before30.home.grpcex.client2.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.CallOptions;
import io.grpc.ClientCall;
import io.grpc.ManagedChannel;
import io.grpc.MethodDescriptor;
import io.grpc.binarylog.v1.MessageOrBuilder;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

/**
 * GrpcClient
 *
 * @author before30
 * @since 2019-07-09
 */
@Slf4j
public class GrpcClient {

    private final MethodDescriptor protoMethodDescriptor;
    private final ManagedChannel channel;

    public GrpcClient(@Nonnull MethodDescriptor protoMethodDescriptor,
                      @Nonnull ManagedChannel channel) {
        this.protoMethodDescriptor = protoMethodDescriptor;
        this.channel = channel;
    }

    public ListenableFuture<Void> call(
            ImmutableList<MessageOrBuilder> requests,
            StreamObserver<MessageOrBuilder> responseObserver,
            CallOptions callOptions) {
        Preconditions.checkArgument(!requests.isEmpty(), "Can't make call without any requests");
        MethodDescriptor.MethodType methodType = getMethodType();
        long numRequests = requests.size();
        if (methodType == MethodDescriptor.MethodType.UNARY) {
            log.info("Making unary call");
            Preconditions.checkArgument(numRequests == 1,
                    "Need exactly 1 request for unary call, but got: " + numRequests);
            return callUnary(requests.get(0), responseObserver, callOptions);
        } else if (methodType == MethodDescriptor.MethodType.SERVER_STREAMING) {
            log.info("Making server streaming call");
            Preconditions.checkArgument(numRequests == 1,
                    "Need exactly 1 request for server streaming call, but got: " + numRequests);
            return callServerStreaming(requests.get(0), responseObserver, callOptions);
        } else if (methodType == MethodDescriptor.MethodType.CLIENT_STREAMING) {
            log.info("Making client streaming call with " + requests.size() + " requests");
            return callClientStreaming(requests, responseObserver, callOptions);
        } else {
            // Bidi streaming.
            log.info("Making bidi streaming call with " + requests.size() + " requests");
            return callBidiStreaming(requests, responseObserver, callOptions);
        }
    }

    private ListenableFuture<Void> callBidiStreaming(
            ImmutableList<MessageOrBuilder> requests,
            StreamObserver<MessageOrBuilder> responseObserver,
            CallOptions callOptions) {
        DoneObserver<MessageOrBuilder> doneObserver = new DoneObserver<>();
        StreamObserver<MessageOrBuilder> requestObserver = ClientCalls.asyncBidiStreamingCall(
                createCall(callOptions),
                CompositeStreamObserver.of(responseObserver, doneObserver));
        requests.forEach(requestObserver::onNext);
        requestObserver.onCompleted();
        return doneObserver.getCompletionFuture();
    }

    private ListenableFuture<Void> callClientStreaming(
            ImmutableList<MessageOrBuilder> requests,
            StreamObserver<MessageOrBuilder> responseObserver,
            CallOptions callOptions) {
        DoneObserver<MessageOrBuilder> doneObserver = new DoneObserver<>();
        StreamObserver<MessageOrBuilder> requestObserver = ClientCalls.asyncClientStreamingCall(
                createCall(callOptions),
                CompositeStreamObserver.of(responseObserver, doneObserver));
        requests.forEach(requestObserver::onNext);
        requestObserver.onCompleted();
        return doneObserver.getCompletionFuture();
    }

    private ListenableFuture<Void> callServerStreaming(
            MessageOrBuilder request,
            StreamObserver<MessageOrBuilder> responseObserver,
            CallOptions callOptions) {
        DoneObserver<MessageOrBuilder> doneObserver = new DoneObserver<>();
        ClientCalls.asyncServerStreamingCall(
                createCall(callOptions),
                request,
                CompositeStreamObserver.of(responseObserver, doneObserver));
        return doneObserver.getCompletionFuture();
    }

    private ListenableFuture<Void> callUnary(
            MessageOrBuilder request,
            StreamObserver<MessageOrBuilder> responseObserver,
            CallOptions callOptions) {
        DoneObserver<MessageOrBuilder> doneObserver = new DoneObserver<>();
        ClientCalls.asyncUnaryCall(
                createCall(callOptions),
                request,
                CompositeStreamObserver.of(responseObserver, doneObserver));
        return doneObserver.getCompletionFuture();
    }

    private ClientCall<MessageOrBuilder, MessageOrBuilder> createCall(CallOptions callOptions) {
        return channel.newCall(createGrpcMethodDescriptor(), callOptions);
    }

    private MethodDescriptor<MessageOrBuilder, MessageOrBuilder> createGrpcMethodDescriptor() {
        return MethodDescriptor.newBuilder()
                .setFullMethodName(getFullMethodName())
                .setType(getMethodType())
                .setRequestMarshaller(protoMethodDescriptor.getRequestMarshaller())
                .setResponseMarshaller(protoMethodDescriptor.getResponseMarshaller())
                .build();
    }

    private String getFullMethodName() {
        String serviceName = protoMethodDescriptor.getServiceName();
        String methodName = protoMethodDescriptor.getFullMethodName();
        return io.grpc.MethodDescriptor.generateFullMethodName(serviceName, methodName);
    }

    /** Returns the appropriate method type based on whether the client or server expect streams. */
    private MethodDescriptor.MethodType getMethodType() {
        boolean clientStreaming = protoMethodDescriptor.getType().clientSendsOneMessage();
        boolean serverStreaming = protoMethodDescriptor.getType().clientSendsOneMessage();

        if (!clientStreaming && !serverStreaming) {
            return MethodDescriptor.MethodType.UNARY;
        } else if (!clientStreaming && serverStreaming) {
            return MethodDescriptor.MethodType.SERVER_STREAMING;
        } else if (clientStreaming && !serverStreaming) {
            return MethodDescriptor.MethodType.CLIENT_STREAMING;
        } else {
            return MethodDescriptor.MethodType.BIDI_STREAMING;
        }
    }
}
