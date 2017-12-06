#ifeq ($(strip $(MAGCOMM_SOS_SUPPORT)), yes)
LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := baidumapapi_v2_4_2 \
    locSDK_3.1 \
    AppOffer_2.4.8 \
    libammsdk

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := SOS
LOCAL_CERTIFICATE := platform

##################################################
#混淆
#LOCAL_PROGUARD_ENABLED := full
#LOCAL_PROGUARD_ENABLED := custom
#LOCAL_PROGUARD_FLAG_FILES := proguard.flags
##################################################

# 拷贝到系统区 begin
PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/libs/armeabi/libBaiduMapSDK_v2_4_2.so:system/lib/libBaiduMapSDK_v2_4_2.so \
	$(LOCAL_PATH)/libs/armeabi/liblocSDK3.so:system/lib/liblocSDK3.so 
# 拷贝到系统区 end

# 打包到APK begin
PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/libs/armeabi/libBaiduMapSDK_v2_4_2.so:obj/lib/libBaiduMapSDK_v2_4_2.so \
	$(LOCAL_PATH)/libs/armeabi/liblocSDK3.so:obj/lib/liblocSDK3.so 
    
LOCAL_JNI_SHARED_LIBRARIES := libBaiduMapSDK_v2_4_2 \
    liblocSDK3 
# 打包到APK end

##################################################

include $(BUILD_PACKAGE)
##################################################
include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := baidumapapi_v2_4_2:libs/baidumapapi_v2_4_2.jar \
    locSDK_3.1:libs/locSDK_3.1.jar \
    AppOffer_2.4.8:libs/AppOffer_2.4.8.jar \
    libammsdk:libs/libammsdk.jar

include $(BUILD_MULTI_PREBUILT)
##################################################

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))

#endif
