package io.spiffe.workloadapi.internal;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

/**
 * ClientInterceptor implementation to add a security header required to connect to the Workload API.
 */
public class SecurityHeaderInterceptor implements ClientInterceptor {

    private static final String SECURITY_HEADER = "workload.spiffe.io";

    /**
     * Intercepts the call to the Workload API and adds the required security header.
     */
    @Override
    public <R,S> ClientCall<R,S> interceptCall(MethodDescriptor<R,S> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<R,S>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<S> responseListener, Metadata headers) {
                Metadata.Key<String> headerKey = Metadata.Key.of(SECURITY_HEADER, Metadata.ASCII_STRING_MARSHALLER);
                headers.put(headerKey, "true");
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<S>(responseListener) {}, headers);
            }
        };
    }
}
