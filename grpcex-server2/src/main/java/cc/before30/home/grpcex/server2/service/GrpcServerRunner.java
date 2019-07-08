package cc.before30.home.grpcex.server2.service;

import cc.before30.home.grpcex.server2.config.GrpcServerProperties;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.services.HealthStatusManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

/**
 * GrpcServerRunner
 *
 * @author before30
 * @since 2019-07-08
 */
@Slf4j
public class GrpcServerRunner implements CommandLineRunner, DisposableBean {

    private final HealthStatusManager healthStatusManager;
    private final GrpcServerProperties grpcServerProperties;
    private Server server;
    private final ServerBuilder<?> serverBuilder;
    private List<BindableService> services;
    private final Executor executor;

    public GrpcServerRunner(HealthStatusManager healthStatusManager,
                            GrpcServerProperties grpcServerProperties,
                            ServerBuilder<?> serverBuilder,
                            List<BindableService> services,
                            Executor executor) {
        this.healthStatusManager = healthStatusManager;
        this.grpcServerProperties = grpcServerProperties;
        this.serverBuilder = serverBuilder;
        this.services = services;
        this.executor = executor;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("## Starting gRPC Server ##");

        serverBuilder.addService(healthStatusManager.getHealthService());

        services.forEach(s -> {
            serverBuilder.addService(s);
            String serviceName = s.getClass().getName();
            healthStatusManager.setStatus(serviceName, HealthCheckResponse.ServingStatus.SERVING);
            log.info("'{}' service has been registered.", serviceName);
        });

        if (grpcServerProperties.isEnableReflection()) {
            serverBuilder.addService(ProtoReflectionService.newInstance());
            log.info("'{}' service has been registered.", ProtoReflectionService.class.getName());
        }

        server = serverBuilder.build().start();

        log.info("gRPC Server started, listening on port {}.", server.getPort());
        startDaemonAwaitThread();

    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(()->{
            try {
                GrpcServerRunner.this.server.awaitTermination();
            } catch (InterruptedException e) {
                log.error("gRPC server stopped.", e);
            }
        });
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
    @Override
    public void destroy() throws Exception {
        Optional.ofNullable(server).ifPresent(s->{
            log.info("Shutting down gRPC server ...");
            s.getServices().forEach(def->healthStatusManager.clearStatus(def.getServiceDescriptor().getName()));
            s.shutdown();
            log.info("gRPC server stopped.");
        });
    }

}
