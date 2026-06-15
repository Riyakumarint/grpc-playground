package greeting.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.out.println("Need one argument to work");
//            return;
//        }
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        System.out.println("Shutting Down");
        channel.shutdown();
    }
}
