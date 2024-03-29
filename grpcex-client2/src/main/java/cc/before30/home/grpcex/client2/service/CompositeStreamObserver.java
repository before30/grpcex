package cc.before30.home.grpcex.client2.service;

import com.google.common.collect.ImmutableList;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * CompositeStreamObserver
 *
 * @author before30
 * @since 2019-07-10
 */

@Slf4j
public class CompositeStreamObserver<T> implements StreamObserver<T> {
    private final ImmutableList<StreamObserver<T>> observers;

    @SafeVarargs
    public static <T> CompositeStreamObserver<T> of(StreamObserver<T>... observers) {
        return new CompositeStreamObserver<T>(ImmutableList.copyOf(observers));
    }

    private CompositeStreamObserver(ImmutableList<StreamObserver<T>> observers) {
        this.observers = observers;
    }

    @Override
    public void onCompleted() {
        for (StreamObserver<T> observer : observers) {
            try {
                observer.onCompleted();
            } catch (Throwable t) {
                log.error("Exception in composite onComplete, moving on", t);
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        for (StreamObserver<T> observer : observers) {
            try {
                observer.onError(t);
            } catch (Throwable s) {
                log.error("Exception in composite onError, moving on", s);
            }
        }
    }

    @Override
    public void onNext(T value) {
        for (StreamObserver<T> observer : observers) {
            try {
                observer.onNext(value);
            } catch (Throwable t) {
                log.error("Exception in composite onNext, moving on", t);
            }
        }
    }
}
