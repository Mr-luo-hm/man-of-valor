// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: message.proto

package com.lyj;

public final class GrpcService {

	private GrpcService() {
	}

	public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {
	}

	public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
		registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
	}

	static final com.google.protobuf.Descriptors.Descriptor internal_static_Request_descriptor;
	static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable internal_static_Request_fieldAccessorTable;
	static final com.google.protobuf.Descriptors.Descriptor internal_static_Response_descriptor;
	static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable internal_static_Response_fieldAccessorTable;

	public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
		return descriptor;
	}

	private static com.google.protobuf.Descriptors.FileDescriptor descriptor;
	static {
		java.lang.String[] descriptorData = { "\n\rmessage.proto\"\032\n\007Request\022\017\n\007request\030\001 "
				+ "\001(\t\"\034\n\010Response\022\020\n\010response\030\001 \001(\t20\n\rCom"
				+ "monService\022\037\n\006method\022\010.Request\032\t.Respons"
				+ "e\"\000B\030\n\007com.lyjB\013GrpcServiceP\001b\006proto3" };
		com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
			public com.google.protobuf.ExtensionRegistry assignDescriptors(
					com.google.protobuf.Descriptors.FileDescriptor root) {
				descriptor = root;
				return null;
			}
		};
		com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData,
				new com.google.protobuf.Descriptors.FileDescriptor[] {}, assigner);
		internal_static_Request_descriptor = getDescriptor().getMessageTypes().get(0);
		internal_static_Request_fieldAccessorTable = new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
				internal_static_Request_descriptor, new java.lang.String[] { "Request", });
		internal_static_Response_descriptor = getDescriptor().getMessageTypes().get(1);
		internal_static_Response_fieldAccessorTable = new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
				internal_static_Response_descriptor, new java.lang.String[] { "Response", });
	}

	// @@protoc_insertion_point(outer_class_scope)

}
