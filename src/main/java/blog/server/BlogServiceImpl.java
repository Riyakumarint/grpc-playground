package blog.server;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import com.proto.blog.Blog;
import com.proto.blog.BlogId;
import com.proto.blog.BlogServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;

public final class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    private final MongoCollection<Document> mongoCollection;

    BlogServiceImpl(MongoClient client) {
        MongoDatabase db = client.getDatabase("blogdb");
        mongoCollection = db.getCollection("blog");
    }

    private io.grpc.StatusRuntimeException error(Status status, String message) {
        return status.withDescription(message).asRuntimeException();
    }

    @Override
    public void createBlog(Blog request, StreamObserver<BlogId> responseObserver) {
        System.out.println("Received Create Blog request");

        Document doc = new Document("author", request.getAuthor())
                .append("title", request.getTitle())
                .append("content", request.getContent());

        System.out.println("Inserting blog...");
        InsertOneResult result;

        try {
            result = mongoCollection.insertOne(doc);
        } catch (MongoException e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getLocalizedMessage()).asRuntimeException());
            return;
        }

        if (!result.wasAcknowledged() || result.getInsertedId() == null) {
            responseObserver.onError(Status.INTERNAL.withDescription("Blog couldn't be created").asRuntimeException());
            return;
        }

        String id = result.getInsertedId().asObjectId().getValue().toString();
        System.out.println("Inserted blog: " + id);

        responseObserver.onNext(BlogId.newBuilder().setId(id).build());
        responseObserver.onCompleted();
    }
}