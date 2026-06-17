package blog.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.Empty;
import com.proto.blog.Blog;
import com.proto.blog.BlogId;
import com.proto.blog.BlogServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;

public final class BlogClient {

    private BlogClient() {}

    private static void run(ManagedChannel channel) {
        BlogServiceGrpc.BlogServiceBlockingStub stub = BlogServiceGrpc.newBlockingStub(channel);

        BlogId id = createBlog(stub);

        if (id == null)
            return;
    }

    static BlogId createBlog(BlogServiceGrpc.BlogServiceBlockingStub stub) {
        System.out.println("Creating blog....");

        try {
            BlogId createResponse = stub.createBlog(
                    Blog.newBuilder()
                            .setAuthor("Clement")
                            .setTitle("New blog!")
                            .setContent("Hello world this is my first blog!")
                            .build()
            );

            System.out.println("Blog created: " + createResponse.getId());
            System.out.println();
            return createResponse;
        } catch (StatusRuntimeException e) {
            System.out.println("Couldn't create the blog");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        run(channel);

        System.out.println("Shutting Down");
        channel.shutdown();
    }
}