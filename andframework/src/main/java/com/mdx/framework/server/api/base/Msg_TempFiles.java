// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/tempfile.proto
package com.mdx.framework.server.api.base;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.List;

import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Label.REPEATED;

public final class Msg_TempFiles extends Message {

  private static final long serialVersionUID = 1L;

  public static final Long DEFAULT_TEMPSIZE = 0L;
  public static final List<Msg_TempFile> DEFAULT_FILES = immutableCopyOf(null);

  @ProtoField(tag = 1, type = INT64)
  public Long tempSize = DEFAULT_TEMPSIZE;

  @ProtoField(tag = 4, label = REPEATED, messageType = Msg_TempFile.class)
  public List<Msg_TempFile> files = immutableCopyOf(null);

  public Msg_TempFiles(Long tempSize, List<Msg_TempFile> files) {
    this.tempSize = tempSize==null?this.tempSize:tempSize;
    this.files = immutableCopyOf(files);
  }

  public Msg_TempFiles() {
  }

  private Msg_TempFiles(Builder builder) {
    this(builder.tempSize, builder.files);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Msg_TempFiles)) return false;
    Msg_TempFiles o = (Msg_TempFiles) other;
    return equals(tempSize, o.tempSize)
        && equals(files, o.files);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = tempSize != null ? tempSize.hashCode() : 0;
      result = result * 37 + (files != null ? files.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<Msg_TempFiles> {
    private static final long serialVersionUID = 1L;


    public Long tempSize;
    public List<Msg_TempFile> files;

    public Builder() {
    }

    public Builder(Msg_TempFiles message) {
      super(message);
      if (message == null) return;
      this.tempSize = message.tempSize;
      this.files = copyOf(message.files);
    }

    @Override
    public Msg_TempFiles build() {
      return new Msg_TempFiles(this);
    }
  }
}
