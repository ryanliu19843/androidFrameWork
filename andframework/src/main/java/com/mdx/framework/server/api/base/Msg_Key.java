// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/AppUpdate.proto
package com.mdx.framework.server.api.base;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

public final class Msg_Key extends Message {

	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_CODE = "";
	public static final String DEFAULT_VALUE = "";

	@ProtoField(tag = 1, type = STRING)
	public String code;

	/**
	 * 键
	 */
	@ProtoField(tag = 2, type = STRING)
	public String value;

	public Msg_Key(String code, String value) {
		this.code = code == null ? this.code : code;
		this.value = value == null ? this.value : value;
	}

	public Msg_Key() {
	}

	private Msg_Key(Builder builder) {
		this(builder.code, builder.value);
		setBuilder(builder);
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!(other instanceof Msg_Key))
			return false;
		Msg_Key o = (Msg_Key) other;
		return equals(code, o.code) && equals(value, o.value);
	}

	@Override
	public int hashCode() {
		int result = hashCode;
		if (result == 0) {
			result = code != null ? code.hashCode() : 0;
			result = result * 37 + (value != null ? value.hashCode() : 0);
			hashCode = result;
		}
		return result;
	}

	public static final class Builder extends Message.Builder<Msg_Key> {
		private static final long serialVersionUID = 1L;

		public String code;
		public String value;

		public Builder() {
		}

		public Builder(Msg_Key message) {
			super(message);
			if (message == null)
				return;
			this.code = message.code;
			this.value = message.value;
		}

		@Override
		public Msg_Key build() {
			return new Msg_Key(this);
		}
	}
}
