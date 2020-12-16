// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/request.proto
package com.mdx.framework.server.api.base;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.List;
import okio.ByteString;

import static com.squareup.wire.Message.Datatype.BYTES;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.REPEATED;

public final class Msg_Request extends Message {

  private static final long serialVersionUID = 1L;

  public static final List<Msg_Post> DEFAULT_POSTS = immutableCopyOf(null);
  public static final ByteString DEFAULT_REQUESTMESSAGE = ByteString.EMPTY;
  public static final List<Msg_Request> DEFAULT_REQUESTS = immutableCopyOf(null);
  public static final String DEFAULT_URL = "";

  @ProtoField(tag = 1, label = REPEATED, messageType = Msg_Post.class)
  public List<Msg_Post> posts = immutableCopyOf(null);

  @ProtoField(tag = 2, type = BYTES)
  public ByteString requestMessage;

  @ProtoField(tag = 3, label = REPEATED, messageType = Msg_Request.class)
  public List<Msg_Request> requests = immutableCopyOf(null);

  @ProtoField(tag = 4, type = STRING)
  public String url;

  public Msg_Request(List<Msg_Post> posts, ByteString requestMessage, List<Msg_Request> requests, String url) {
    this.posts = immutableCopyOf(posts);
    this.requestMessage = requestMessage==null?this.requestMessage:requestMessage;
    this.requests = immutableCopyOf(requests);
    this.url = url==null?this.url:url;
  }

  public Msg_Request() {
  }

  private Msg_Request(Builder builder) {
    this(builder.posts, builder.requestMessage, builder.requests, builder.url);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Msg_Request)) return false;
    Msg_Request o = (Msg_Request) other;
    return equals(posts, o.posts)
        && equals(requestMessage, o.requestMessage)
        && equals(requests, o.requests)
        && equals(url, o.url);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = posts != null ? posts.hashCode() : 1;
      result = result * 37 + (requestMessage != null ? requestMessage.hashCode() : 0);
      result = result * 37 + (requests != null ? requests.hashCode() : 1);
      result = result * 37 + (url != null ? url.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<Msg_Request> {
    private static final long serialVersionUID = 1L;


    public List<Msg_Post> posts;
    public ByteString requestMessage;
    public List<Msg_Request> requests;
    public String url;

    public Builder() {
    }

    public Builder(Msg_Request message) {
      super(message);
      if (message == null) return;
      this.posts = copyOf(message.posts);
      this.requestMessage = message.requestMessage;
      this.requests = copyOf(message.requests);
      this.url = message.url;
    }

    @Override
    public Msg_Request build() {
      return new Msg_Request(this);
    }
  }
}