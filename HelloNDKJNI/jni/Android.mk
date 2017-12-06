LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := hello-ndk-jni

LOCAL_SRC_FILES := hello-ndk-jni.c

include $(BUILD_SHARED_LIBRARY)